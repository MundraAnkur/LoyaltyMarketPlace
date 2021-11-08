package com.example.marketplace.model;

import java.sql.Timestamp;

/**
 * @author ankurmundra
 * November 03, 2021
 */
public class Wallet {
    private long id;
    private String walletId;
    private String lpCode;
    private String category;
    private String activityName;
    private String ruleCode;
    private int points;
    private Timestamp timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getLpCode() {
        return lpCode;
    }

    public void setLpCode(String lpCode) {
        this.lpCode = lpCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", walletId='" + walletId + '\'' +
                ", lpCode='" + lpCode + '\'' +
                ", category='" + category + '\'' +
                ", activityName='" + activityName + '\'' +
                ", ruleCode='" + ruleCode + '\'' +
                ", points=" + points +
                ", timestamp=" + timestamp +
                '}';
    }
}
