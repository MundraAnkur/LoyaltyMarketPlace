package com.example.marketplace.model;

public class Reward {
    private String lpCode;
    private String rewardId;
    private String rewardName;

    public String getLpCode() {
        return lpCode;
    }

    public void setLpCode(String lpCode) {
        this.lpCode = lpCode;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "lpCode='" + lpCode + '\'' +
                ", rewardId='" + rewardId + '\'' +
                ", rewardName='" + rewardName + '\'' +
                '}';
    }
}
