package com.example.marketplace.model;

public class RewardCategory {
    private String rewardId;
    private String rewardName;

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
        return "RewardTypeGlobal{" +
                "rewardId='" + rewardId + '\'' +
                ", rewardName='" + rewardName + '\'' +
                '}';
    }
}
