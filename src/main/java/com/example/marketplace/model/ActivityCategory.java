package com.example.marketplace.model;

public class ActivityCategory {
    private String activityId;
    private String activityName;

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
        return "ActivityCategory{" +
                "activityId='" + activityId + '\'' +
                ", activityName='" + activityName + '\'' +
                '}';
    }
}
