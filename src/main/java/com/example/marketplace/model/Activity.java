package com.example.marketplace.model;

public class Activity {
    private String lpCode;
    private String activityId;
    private String activityName;

    public String getLpCode() {
        return lpCode;
    }

    public void setLpCode(String lpCode) {
        this.lpCode = lpCode;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "lpCode='" + lpCode + '\'' +
                ", activityId='" + activityId + '\'' +
                ", activityName='" + activityName + '\'' +
                '}';
    }
}
