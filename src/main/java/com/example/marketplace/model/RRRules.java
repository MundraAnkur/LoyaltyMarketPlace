package com.example.marketplace.model;

public class RRRules {
    private String ruleCode;
    private String lpCode;
    private String rewardName;
    private int points;
    private int instances;
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

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "RRRules{" +
                "ruleCode='" + ruleCode + '\'' +
                ", lpCode='" + lpCode + '\'' +
                ", rewardName='" + rewardName + '\'' +
                ", points=" + points +
                ", instances=" + instances +
                ", version=" + version +
                '}';
    }
}
