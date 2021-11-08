package com.example.marketplace.model;

public class RERules {
    private String ruleCode;
    private String lpCode;
    private String activityName;
    private int points;
    private int version;

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getLpCode() {
        return lpCode;
    }

    public void setLpCode(String lpCode) {
        this.lpCode = lpCode;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "RERules{" +
                "ruleCode='" + ruleCode + '\'' +
                ", lpCode='" + lpCode + '\'' +
                ", activityName='" + activityName + '\'' +
                ", points=" + points +
                ", version=" + version +
                '}';
    }
}
