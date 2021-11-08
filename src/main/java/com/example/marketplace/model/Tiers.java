package com.example.marketplace.model;

public class Tiers {
    private String lpCode;
    private String tierName;
    private int level;
    private int multiplier;
    private int pointsRequired;

    public String getLpCode() {
        return lpCode;
    }

    public void setLpCode(String lpCode) {
        this.lpCode = lpCode;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    @Override
    public String toString() {
        return "Tiers{" +
                "lpCode='" + lpCode + '\'' +
                ", tierName='" + tierName + '\'' +
                ", level=" + level +
                ", multiplier=" + multiplier +
                ", pointsRequired=" + pointsRequired +
                '}';
    }
}
