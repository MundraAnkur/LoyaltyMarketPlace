CREATE OR REPLACE TRIGGER ADD_SPECIAL_ACTIVITY_JOIN AFTER INSERT ON ENROLL_CUSTOMER FOR EACH ROW
DECLARE
    walletId VARCHAR2(20);
    tier_t VARCHAR2(10) default null;
    is_tiered NUMBER(1,0);
BEGIN
    SELECT C.WALLET_ID INTO walletId FROM CUSTOMER C where C.CUSTOMER_ID = :new.CUSTOMER_ID;
    SELECT L.IS_TIERED INTO is_tiered FROM LOYALTY_PROGRAM L WHERE L.CODE = :new.LP_CODE;
    IF(is_tiered = 1) THEN
        SELECT T.TIER_NAME INTO tier_t FROM TIERS T WHERE T.LP_CODE = :new.LP_CODE and T."LEVEL" = 0;
    END IF;

    INSERT INTO CUSTOMER_PROGRAM_STATUS VALUES (walletId,:new.LP_CODE,0,tier_t);

    INSERT INTO WALLET (WALLET_ID, LP_CODE, "CATEGORY", ACTIVITY_NAME, RULE_CODE, POINTS,"DATE")
    values (walletId,:new.LP_CODE,'ENROLL','JOIN',null,0,current_timestamp);
END;
/

CREATE OR REPLACE TRIGGER UPDATE_CUSTOMER_TIER_STATUS_AND_LOYALTY_POINTS AFTER INSERT ON WALLET FOR EACH ROW
DECLARE
    current_tier VARCHAR2(10) default null;
    required_points NUMBER(10);
    cumulative_points NUMBER(10);
    current_level INT;
    tier VARCHAR2(10);
BEGIN
    SELECT CP.TIER_STATUS, CP.TOTAL_POINTS INTO current_tier, cumulative_points FROM CUSTOMER_PROGRAM_STATUS CP WHERE CP.WALLET_ID = :new.WALLET_ID and CP.LP_CODE = :new.LP_CODE;
    cumulative_points := cumulative_points + :new.POINTS;
    IF(current_tier IS NOT NULL) THEN
        SELECT T."LEVEL" INTO current_level FROM TIERS T WHERE T.LP_CODE = :new.LP_CODE and T.TIER_NAME = current_tier;
        SELECT T.TIER_NAME, T.POINTS_REQUIRED INTO tier, required_points FROM TIERS T WHERE T.LP_CODE = :new.LP_CODE and T."LEVEL" = current_level+1;
        IF (cumulative_points >= required_points) THEN
            UPDATE CUSTOMER_PROGRAM_STATUS CP SET CP.TIER_STATUS = tier WHERE CP.WALLET_ID = :new.WALLET_ID and CP.LP_CODE = :new.LP_CODE;
        END IF;
    END IF;
    UPDATE CUSTOMER_PROGRAM_STATUS CP SET CP.TOTAL_POINTS = cumulative_points WHERE CP.WALLET_ID = :new.WALLET_ID and CP.LP_CODE = :new.LP_CODE;
END;
/

CREATE OR REPLACE FUNCTION CAN_REWARD_BE_REDEEMED (lpCode IN VARCHAR2, rewardName IN VARCHAR2, walledId IN VARCHAR2) RETURN INT
    IS
    available_instances NUMBER;
    points_required NUMBER;
    available_points NUMBER;
BEGIN
    SELECT  RR.INSTANCES, RR.POINTS INTO available_instances, points_required
    FROM RR_RULES RR WHERE RR.LP_CODE = lpCode and RR.REWARD_NAME = rewardName;
    IF (available_instances < 1) THEN
        return -1;
    ELSE
        SELECT C.TOTAL_POINTS INTO available_points FROM CUSTOMER_PROGRAM_STATUS C
        WHERE C.WALLET_ID = walledId and C.LP_CODE = lpCode;
        IF (available_points < points_required) THEN
            return 0;
        END IF;
    END IF;
    return 1;
END;
/

CREATE OR REPLACE TRIGGER WALLET_ACTIVITY_INSERTION BEFORE INSERT ON WALLET FOR EACH ROW
DECLARE
    can_redeem INT;
    multiplier INT default 1;
    is_tiered NUMBER(1,0);
    point_req NUMBER(10) default 0;
    tier varchar2(10) default null;
    rule_id VARCHAR2(10) default null;
BEGIN
    IF (:new.CATEGORY = 'REDEEM') THEN
        can_redeem := CAN_REWARD_BE_REDEEMED(:new.LP_CODE,:new.ACTIVITY_NAME,:new.WALLET_ID);
        IF (can_redeem = -1) THEN
            RAISE_APPLICATION_ERROR(-20020,'Reward can not be redeemed, no more reward instances available.',False);
            return;
        ELSIF (can_redeem = 0) THEN
            RAISE_APPLICATION_ERROR(-20020,'Reward can not be redeemed, insufficient points to redeem reward.',False);
            return;
        ELSE
            SELECT  RR.RULE_CODE, -1*RR.POINTS INTO rule_id, point_req
            FROM RR_RULES RR WHERE RR.LP_CODE = :new.LP_CODE and RR.REWARD_NAME = :new.ACTIVITY_NAME;
            UPDATE RR_RULES RR SET RR.INSTANCES = RR.INSTANCES -1 WHERE RR.LP_CODE = :new.LP_CODE and RR.REWARD_NAME = :new.ACTIVITY_NAME;
        end if;
    ELSIF (:new.CATEGORY = 'EARN') THEN
        SELECT RE.RULE_CODE, RE.POINTS INTO rule_id, point_req FROM RE_RULES RE
        WHERE RE.LP_CODE = :new.LP_CODE and RE.ACTIVITY_NAME = :new.ACTIVITY_NAME;
        SELECT L.IS_TIERED INTO is_tiered FROM LOYALTY_PROGRAM L WHERE L.CODE = :new.LP_CODE;
        IF(is_tiered = 1) THEN
            SELECT C.TIER_STATUS INTO tier FROM CUSTOMER_PROGRAM_STATUS C
            WHERE C.WALLET_ID = :new.WALLET_ID and C.LP_CODE = :new.LP_CODE;
            SELECT T.MULTIPLIER INTO multiplier FROM TIERS T WHERE T.TIER_NAME = tier;
        END IF;
    END IF;
    :new.POINTS := point_req * multiplier;
    :new.RULE_CODE := rule_id;
    :new.DATE := current_timestamp;
END;
/