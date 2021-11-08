INSERT INTO BRANDS values ('B01','2376 Champion Ct','Puma',current_date);
INSERT INTO BRANDS values ('B02','76 Anavrin Point','Anavrin',current_date);

INSERT INTO LOYALTY_PROGRAM values ('LTP_B01','SportGoods','B01',1,0);
INSERT INTO LOYALTY_PROGRAM values ('LTP_B02','Grocery Store','B02',0,0);

INSERT INTO CUSTOMER values ('C0001','20 Ingram Street, NY','Peter Parker',8636234678,'W0001');
INSERT INTO CUSTOMER values ('C0002','560 Lemon Street, NC','Steve Smith',8676547657,'W0002');

INSERT INTO ACTIVITY_CATEGORY values ('A001','Purchase');
INSERT INTO ACTIVITY_CATEGORY values ('A002','Write a review');
INSERT INTO ACTIVITY_CATEGORY values ('A003','Refer a friend');

INSERT INTO REWARD_CATEGORY values ('RW001','Gift Card');
INSERT INTO REWARD_CATEGORY values ('RW002','Free Product');

INSERT INTO REWARD values ('LTP_B01','RW001','Gift Card');
INSERT INTO REWARD values ('LTP_B01','RW002','Free Product');
INSERT INTO REWARD values ('LTP_B02','RW001','Gift Card');

INSERT INTO ACTIVITY values ('LTP_B01','A001','Purchase');
INSERT INTO ACTIVITY values ('LTP_B02','A001','Purchase');
INSERT INTO ACTIVITY values ('LTP_B02','A002','Write a review');

INSERT INTO TIERS values ('LTP_B01','Bronze',0,0,1);
INSERT INTO TIERS values ('LTP_B01','Silver',1,170,2);
INSERT INTO TIERS values ('LTP_B01','Gold',2,270,3);

INSERT INTO RE_RULES values ('RER01','LTP_B01','Purchase',15,1);
INSERT INTO RE_RULES values ('RER02','LTP_B02','Purchase',40,1);
INSERT INTO RE_RULES values ('RER03','LTP_B02','Write a review',30,1);

INSERT INTO RR_RULES values ('RRR01','LTP_B01','Gift Card',80,40,1);
INSERT INTO RR_RULES values ('RRR02','LTP_B01','Free Product',70,25,1);
INSERT INTO RR_RULES values ('RRR03','LTP_B02','Gift Card',120,30,1);

INSERT INTO ENROLL_CUSTOMER values ('C0001','LTP_B01');
INSERT INTO ENROLL_CUSTOMER values ('C0001','LTP_B02');
INSERT INTO ENROLL_CUSTOMER values ('C0002','LTP_B01');