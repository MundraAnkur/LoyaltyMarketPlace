package com.example.marketplace.util;

import com.example.marketplace.model.ActivityCategory;
import com.example.marketplace.model.Brands;
import com.example.marketplace.model.Customer;
import com.example.marketplace.model.CustomerProgramStatus;
import com.example.marketplace.model.LoyaltyProgram;
import com.example.marketplace.model.RERules;
import com.example.marketplace.model.RRRules;
import com.example.marketplace.model.RewardCategory;
import com.example.marketplace.model.Tiers;
import com.example.marketplace.model.Wallet;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ankurmundra
 * November 06, 2021
 */
public class ApplicationDao {

    public static Map<String, String> queries = new LinkedHashMap<>();

    static {
        queries.put("List all customers that are not part of Brand02’s program", "SELECT DISTINCT C.CUSTOMER_ID, C.NAME FROM CUSTOMER C, ENROLL_CUSTOMER EC, LOYALTY_PROGRAM LP " +
                "WHERE LP.BRAND_ID = 'B02' and LP.CODE = EC.LP_CODE and EC.CUSTOMER_ID <> C.CUSTOMER_ID");
        queries.put("List the rewards that are part of Brand01 loyalty program", "SELECT DISTINCT R.REWARD_ID, R.REWARD_NAME FROM REWARD R, LOYALTY_PROGRAM L WHERE L.BRAND_ID = 'B01' and R.LP_CODE = L.CODE");
        queries.put("List customers of Brand01 that have redeemed at least twice", "SELECT DISTINCT C.CUSTOMER_ID, C.NAME FROM CUSTOMER C WHERE C.WALLET_ID IN " +
                "(SELECT W.WALLET_ID FROM WALLET W WHERE W.CATEGORY = 'REDEEM' and W.LP_CODE = (SELECT CODE FROM LOYALTY_PROGRAM WHERE BRAND_ID = 'B01') GROUP BY (W.WALLET_ID) HAVING COUNT(*) > 1)");
        queries.put("All brands where total number of points redeemed overall is less than 500 points", "SELECT DISTINCT B.BRAND_ID, B.BRAND_NAME FROM BRANDS B, LOYALTY_PROGRAM L " +
                "WHERE B.BRAND_ID = L.BRAND_ID and L.CODE IN (SELECT LP_CODE FROM WALLET WHERE CATEGORY = 'REDEEM' GROUP BY LP_CODE HAVING -1*SUM(POINTS) < 500)");
        queries.put("For Brand01, list for each activity type in their loyalty program, the number instances that have occurred", "SELECT W.ACTIVITY_NAME, COUNT(*) AS FREQUENCY " +
                "FROM WALLET W WHERE W.CATEGORY = 'EARN' and W.LP_CODE = (SELECT CODE FROM LOYALTY_PROGRAM WHERE BRAND_ID = 'B02') GROUP BY W.ACTIVITY_NAME");
        queries.put("List customers that have joined a loyalty program but have not participated in any activity in that program",
                "SELECT DISTINCT EC.CUSTOMER_ID, EC.LP_CODE FROM ENROLL_CUSTOMER EC WHERE EXISTS(SELECT W.WALLET_ID FROM WALLET W, CUSTOMER C WHERE EC.CUSTOMER_ID = C.CUSTOMER_ID" +
                        " and C.WALLET_ID = W.WALLET_ID and W.LP_CODE = EC.LP_CODE GROUP BY W.WALLET_ID, W.LP_CODE HAVING COUNT(*) < 2)");
        queries.put("For Customer C0003, and Brand02, number of activities they have done in the period of 08/1/2021 and 9/30/2021",
                "SELECT COUNT(*) AS NUMBER_OF_ACTIVITIES FROM WALLET W WHERE W.LP_CODE = (SELECT CODE FROM LOYALTY_PROGRAM WHERE BRAND_ID = 'B02') and W.WALLET_ID = " +
                        "(SELECT WALLET_ID FROM CUSTOMER WHERE CUSTOMER_ID = 'C0003') " +
                        "and W.\"DATE\" >= TO_DATE('11/06/2021', 'MM/DD/YYYY') and W.\"DATE\" < TO_DATE('11/08/2021', 'MM/DD/YYYY')\n");
        queries.put("List all the loyalty programs that include “refer a friend” as an activity in at least one of their reward rules",
                "SELECT CODE, NAME FROM LOYALTY_PROGRAM WHERE CODE IN (SELECT LP_CODE FROM RE_RULES WHERE ACTIVITY_NAME = 'Refer a friend')");
    }

    public static void saveActivityCategory(JdbcTemplate jdbcTemplate, ActivityCategory category){
        jdbcTemplate.update("INSERT INTO ACTIVITY_CATEGORY values (?,?)"
                , category.getActivityId(), category.getActivityName());
    }

    public static void saveRewardCategory(JdbcTemplate jdbcTemplate, RewardCategory category) {
        jdbcTemplate.update("INSERT INTO REWARD_CATEGORY values (?,?)"
                , category.getRewardId(), category.getRewardName());
    }

    public static void saveLoyaltyProgram(JdbcTemplate jdbcTemplate, LoyaltyProgram program) {
        jdbcTemplate.update("INSERT INTO LOYALTY_PROGRAM(CODE, NAME, BRAND_ID, IS_TIERED, IS_VALIDATED) values (?,?,?,?,?)"
                , program.getCode(), program.getName(),program.getBrandId(),program.getIsTiered(),program.getIsValidated());
    }


    public static String getLoyaltyProgramCodeFromBrandId(JdbcTemplate jdbcTemplate, String brandId)
    {
        String sql = "SELECT L.CODE FROM LOYALTY_PROGRAM L WHERE L.BRAND_ID = ?";
        return jdbcTemplate.queryForObject(sql,String.class,brandId);
    }

    public static String getLoggedInUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails)authentication.getPrincipal()).getUsername();
    }

    public static String getLoggedInCustomerOrBrand(JdbcTemplate jdbcTemplate)
    {
        String user = getLoggedInUser();
        return jdbcTemplate.queryForObject("SELECT ROLE_ID FROM MARKET_PLACE_USER WHERE USER_NAME = ?",String.class,user);
    }

//    public static List<String> getAllLoyaltyProgram(JdbcTemplate jdbcTemplate) throws Exception
//    {
//        return jdbcTemplate.queryForList("SELECT NAME FROM LOYALTY_PROGRAM",String.class);
//    }

    public static List<LoyaltyProgram> getLoyaltyProgramsNotEnrolled(JdbcTemplate jdbcTemplate, String cid)
    {
        String lQuery = "SELECT CODE, NAME, BRAND_ID, IS_TIERED, IS_VALIDATED FROM LOYALTY_PROGRAM WHERE CODE NOT IN " +
                "(SELECT LP_CODE FROM ENROLL_CUSTOMER WHERE CUSTOMER_ID = ?)";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(lQuery,cid);
        return convertLoyaltyMapToList(list);
    }

    public static List<LoyaltyProgram> getCustomerEnrolledPrograms(JdbcTemplate jdbcTemplate, String customerId)
    {
        String lQuery = "SELECT L.CODE, L.NAME, L.BRAND_ID, L.IS_TIERED, L.IS_VALIDATED FROM LOYALTY_PROGRAM L WHERE L.CODE IN " +
                "(SELECT LP_CODE FROM ENROLL_CUSTOMER WHERE CUSTOMER_ID = ?)";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(lQuery,customerId);
        return convertLoyaltyMapToList(list);
    }

    public static List<String> getProgramActivities(JdbcTemplate jdbcTemplate, String lpCode)
    {
        return jdbcTemplate.queryForList("SELECT ACTIVITY_NAME FROM ACTIVITY WHERE LP_CODE = ?",
                String.class,lpCode);
    }

    public static List<String> getProgramRewards(JdbcTemplate jdbcTemplate, String lpCode)
    {
        return jdbcTemplate.queryForList("SELECT REWARD_NAME FROM REWARD WHERE LP_CODE = ?",
                String.class,lpCode);
    }

    public static String getProgramCode(JdbcTemplate jdbcTemplate, String programName) throws Exception
    {
        return jdbcTemplate.queryForObject("SELECT CODE FROM LOYALTY_PROGRAM WHERE NAME = ?",String.class, programName);
    }

    public static void enrollCustomer(JdbcTemplate jdbcTemplate, String cid, String lpCode)
    {
        jdbcTemplate.update("INSERT INTO ENROLL_CUSTOMER(CUSTOMER_ID, LP_CODE) values (?,?)",cid,lpCode);
    }

    public static String getWalletId(JdbcTemplate jdbcTemplate, String cid)
    {
        return jdbcTemplate.queryForObject("SELECT WALLET_ID FROM CUSTOMER WHERE CUSTOMER_ID = ?",String.class,cid);
    }

    public static List<Wallet> viewWalletData(JdbcTemplate jdbcTemplate, String cid)
    {
        String walletId = getWalletId(jdbcTemplate,cid);
        String walletQuery = "SELECT LP_CODE, CATEGORY, ACTIVITY_NAME, RULE_CODE, POINTS, \"DATE\" FROM WALLET WHERE WALLET_ID = ?";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(walletQuery,walletId);
        List<Wallet> lt = new ArrayList<>();

        for (Map<String,Object> map : list) {
            Wallet wallet = new Wallet();
            wallet.setLpCode((String) map.get("LP_CODE"));
            wallet.setCategory((String) map.get("CATEGORY"));
            wallet.setActivityName((String) map.get("ACTIVITY_NAME"));
            wallet.setRuleCode((String) map.get("RULE_CODE"));
            wallet.setPoints(((BigDecimal) map.get("POINTS")).intValue());
            wallet.setTimestamp((Timestamp) map.get("DATE"));
            lt.add(wallet);
        }
        return lt;
    }

    public static void performCustomerTransaction(JdbcTemplate jdbcTemplate, Wallet wallet)
    {
        jdbcTemplate.update("INSERT INTO WALLET(WALLET_ID, LP_CODE, CATEGORY, ACTIVITY_NAME) values(?,?,?,?)",
                wallet.getWalletId(),wallet.getLpCode(),wallet.getCategory(),wallet.getActivityName());
    }


    public static List<Customer> getAllCustomers(JdbcTemplate jdbcTemplate)
    {
        String cQuery = "SELECT CUSTOMER_ID, ADDRESS, NAME, PHONE, WALLET_ID FROM CUSTOMER";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(cQuery);
        List<Customer> customers  = new ArrayList<>();

        for (Map<String, Object> map: list) {
            Customer customer = new Customer();
            customer.setCustomerId((String) map.get("CUSTOMER_ID"));
            customer.setName((String) map.get("NAME"));
            customer.setAddress((String) map.get("ADDRESS"));
            customer.setPhone(((BigDecimal) map.get("PHONE")).longValue());
            customer.setWalletId((String) map.get("WALLET_ID"));
            customers.add(customer);
        }
        return customers;
    }

    public static Customer getCustomer(JdbcTemplate jdbcTemplate, String cid)
    {
        String cQuery = "SELECT CUSTOMER_ID, ADDRESS, NAME, PHONE, WALLET_ID FROM CUSTOMER WHERE CUSTOMER_ID = ?";
        Map<String,Object> map = jdbcTemplate.queryForList(cQuery,cid).get(0);
        Customer customer = new Customer();
        customer.setCustomerId(cid);
        customer.setName((String) map.get("NAME"));
        customer.setAddress((String) map.get("ADDRESS"));
        customer.setWalletId((String) map.get("WALLET_ID"));
        customer.setPhone(((BigDecimal) map.get("PHONE")).longValue());
        return customer;
    }

    public static List<CustomerProgramStatus> getCustomerStatus(JdbcTemplate jdbcTemplate, String cid)
    {
        String walletId = getWalletId(jdbcTemplate,cid);
        String cQuery = "SELECT WALLET_ID, LP_CODE, TOTAL_POINTS, TIER_STATUS FROM CUSTOMER_PROGRAM_STATUS WHERE WALLET_ID = ?";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(cQuery,walletId);

        List<CustomerProgramStatus> customerProgramStatuses = new ArrayList<>();
        for (Map<String,Object> map : list) {
            CustomerProgramStatus status = new CustomerProgramStatus();
            status.setWalletId((String) map.get("WALLET_ID"));
            status.setLpCode((String) map.get("LP_CODE"));
            status.setTotalPoints(((BigDecimal) map.get("TOTAL_POINTS")).intValue());
            status.setTierStatus((String) map.get("TIER_STATUS"));
            customerProgramStatuses.add(status);
        }
        return customerProgramStatuses;
    }

    public static List<Brands> getAllBrands(JdbcTemplate jdbcTemplate)
    {
        String bQuery = "SELECT BRAND_ID, ADDRESS, BRAND_NAME, JOIN_DATE FROM BRANDS";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(bQuery);
        List<Brands> brandsList  = new ArrayList<>();

        for (Map<String, Object> map: list) {
            Brands brands = new Brands();
            brands.setBrandId((String) map.get("BRAND_ID"));
            brands.setBrandName((String) map.get("BRAND_NAME"));
            brands.setAddress((String) map.get("ADDRESS"));
            Timestamp timestamp = (Timestamp) map.get("JOIN_DATE");
            brands.setJoinDate(new Date(timestamp.getTime()));
            brandsList.add(brands);
        }
        return brandsList;
    }

    public static Brands getBrand(JdbcTemplate jdbcTemplate, String bid)
    {
        String cQuery = "SELECT BRAND_ID, ADDRESS, BRAND_NAME, JOIN_DATE FROM BRANDS WHERE BRAND_ID = ?";
        Map<String,Object> map = jdbcTemplate.queryForList(cQuery,bid).get(0);
        Brands brands = new Brands();
        brands.setBrandId(bid);
        brands.setBrandName((String) map.get("BRAND_NAME"));
        brands.setAddress((String) map.get("ADDRESS"));
        Timestamp timestamp = (Timestamp) map.get("JOIN_DATE");
        brands.setJoinDate(new Date(timestamp.getTime()));
        return brands;
    }

    public static LoyaltyProgram getLoyaltyProgram(JdbcTemplate jdbcTemplate, String bid)
    {
        String lQuery = "SELECT CODE, NAME, BRAND_ID, IS_TIERED, IS_VALIDATED FROM LOYALTY_PROGRAM WHERE BRAND_ID = ?";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(lQuery,bid);
        LoyaltyProgram program = new LoyaltyProgram();
        if(list.isEmpty())
            return null;
        Map<String,Object> map = list.get(0);
        program.setCode((String) map.get("CODE"));
        program.setName((String) map.get("NAME"));
        program.setBrandId((String) map.get("BRAND_ID"));
        program.setIsTiered(((BigDecimal) map.get("IS_TIERED")).intValue());
        program.setIsValidated(((BigDecimal) map.get("IS_VALIDATED")).intValue());
        return program;
    }

    private static List<LoyaltyProgram> convertLoyaltyMapToList(List<Map<String,Object>> list)
    {
        List<LoyaltyProgram> lt = new ArrayList<>();
        for (Map<String,Object> map : list) {
            LoyaltyProgram program = new LoyaltyProgram();
            program.setCode((String) map.get("CODE"));
            program.setName((String) map.get("NAME"));
            program.setBrandId((String) map.get("BRAND_ID"));
            program.setIsTiered(((BigDecimal) map.get("IS_TIERED")).intValue());
            program.setIsValidated(((BigDecimal) map.get("IS_VALIDATED")).intValue());
            lt.add(program);
        }
        return lt;
    }

    public static List<Tiers> getAllTiersForProgram(JdbcTemplate jdbcTemplate, String lp)
    {
        String sql = "SELECT LP_CODE, TIER_NAME, \"LEVEL\", POINTS_REQUIRED, MULTIPLIER FROM TIERS WHERE LP_CODE = ?";
        List<Map<String , Object>> mapList = jdbcTemplate.queryForList(sql,lp);
        List<Tiers> tiersList = new ArrayList<>();
        for (Map<String,Object> map : mapList) {
            Tiers tiers = new Tiers();
            tiers.setLpCode(lp);
            tiers.setTierName((String) map.get("TIER_NAME"));
            tiers.setLevel(((BigDecimal)map.get("LEVEL")).intValue());
            tiers.setMultiplier(((BigDecimal)map.get("MULTIPLIER")).intValue());
            tiers.setPointsRequired(((BigDecimal)map.get("POINTS_REQUIRED")).intValue());
            tiersList.add(tiers);
        }
        return tiersList;
    }

    public static List<RERules> getAllRERulesForProgram(JdbcTemplate jdbcTemplate, String lp)
    {
        String sql = "SELECT RULE_CODE, LP_CODE, ACTIVITY_NAME, POINTS, VERSION FROM RE_RULES WHERE LP_CODE = ?";
        List<Map<String , Object>> mapList = jdbcTemplate.queryForList(sql,lp);
        List<RERules> reRulesList = new ArrayList<>();
        for (Map<String,Object> map : mapList) {
            RERules rules = new RERules();
            rules.setLpCode(lp);
            rules.setRuleCode((String) map.get("RULE_CODE"));
            rules.setActivityName((String) map.get("ACTIVITY_NAME"));
            rules.setPoints(((BigDecimal)map.get("POINTS")).intValue());
            rules.setVersion(((BigDecimal)map.get("VERSION")).intValue());
            reRulesList.add(rules);
        }
        return reRulesList;
    }

    public static List<RRRules> getAllRRRulesForProgram(JdbcTemplate jdbcTemplate, String lp)
    {
        String sql = "SELECT RULE_CODE, LP_CODE, REWARD_NAME, POINTS, INSTANCES, VERSION FROM RR_RULES WHERE LP_CODE = ?";
        List<Map<String , Object>> mapList = jdbcTemplate.queryForList(sql,lp);
        List<RRRules> rrRulesList = new ArrayList<>();
        for (Map<String,Object> map : mapList) {
            RRRules rules = new RRRules();
            rules.setLpCode(lp);
            rules.setRuleCode((String) map.get("RULE_CODE"));
            rules.setRewardName((String) map.get("REWARD_NAME"));
            rules.setPoints(((BigDecimal)map.get("POINTS")).intValue());
            rules.setVersion(((BigDecimal)map.get("VERSION")).intValue());
            rules.setInstances(((BigDecimal)map.get("INSTANCES")).intValue());
            rrRulesList.add(rules);
        }
        return rrRulesList;
    }

    public static List<String> getAllActivities(JdbcTemplate jdbcTemplate)
    {
        return jdbcTemplate.queryForList("SELECT ACTIVITY_NAME FROM ACTIVITY_CATEGORY",String.class);
    }

    public static String getActivityId(JdbcTemplate jdbcTemplate, String activityName)
    {
        return jdbcTemplate.queryForObject("SELECT ACTIVITY_ID FROM ACTIVITY_CATEGORY WHERE ACTIVITY_NAME = ?",String.class,activityName);
    }

    public static void enrollBrandActivities(JdbcTemplate jdbcTemplate, String activityId, String activityName, String code)
    {
        jdbcTemplate.update("INSERT INTO ACTIVITY (LP_CODE, ACTIVITY_ID, ACTIVITY_NAME) VALUES (?, ?, ?)",code,activityId,activityName);
    }

    public static List<String> getAllRewards(JdbcTemplate jdbcTemplate)
    {
        return jdbcTemplate.queryForList("SELECT REWARD_NAME FROM REWARD_CATEGORY",String.class);
    }

    public static String getRewardId(JdbcTemplate jdbcTemplate, String rewardName)
    {
        return jdbcTemplate.queryForObject("SELECT REWARD_ID FROM REWARD_CATEGORY WHERE REWARD_NAME = ?",String.class,rewardName);
    }

    public static void enrollBrandRewards(JdbcTemplate jdbcTemplate, String rewardId, String rewardName, String code)
    {
        jdbcTemplate.update("INSERT INTO REWARD (LP_CODE, REWARD_ID, REWARD_NAME) VALUES (?, ?, ?)",code,rewardId,rewardName);
    }

    public static void addRERule(JdbcTemplate jdbcTemplate, RERules reRules) {
        jdbcTemplate.update("INSERT INTO RE_RULES(RULE_CODE, LP_CODE, ACTIVITY_NAME, POINTS, VERSION) values (?,?,?,?,?)"
                , reRules.getRuleCode(), reRules.getLpCode(), reRules.getActivityName(), reRules.getPoints(), reRules.getVersion());
    }

    public static void addRRRule(JdbcTemplate jdbcTemplate, RRRules rrRules){
        jdbcTemplate.update("INSERT INTO RR_RULES(RULE_CODE, LP_CODE, REWARD_NAME, POINTS, INSTANCES, VERSION) values (?,?,?,?,?,?)"
                , rrRules.getRuleCode(), rrRules.getLpCode(), rrRules.getRewardName(), rrRules.getPoints(), rrRules.getInstances(), rrRules.getVersion());
    }

    public static void addTiers(JdbcTemplate jdbcTemplate, List<Tiers> tiersList)
    {
        String query = "INSERT INTO TIERS(LP_CODE, TIER_NAME, \"LEVEL\", POINTS_REQUIRED, MULTIPLIER) VALUES (?,?,?,?,?)";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Tiers tiers = tiersList.get(i);
                ps.setString(1,tiers.getLpCode());
                ps.setString(2,tiers.getTierName());
                ps.setInt(3,tiers.getLevel());
                ps.setInt(4,tiers.getPointsRequired());
                ps.setInt(5,tiers.getMultiplier());
            }

            @Override
            public int getBatchSize() {
                return tiersList.size();
            }
        });
    }
}
