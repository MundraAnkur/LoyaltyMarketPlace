package com.example.marketplace.model;

/**
 * @author ankurmundra
 * November 03, 2021
 */
public class CustomerProgramStatus {
    private String walletId;
    private String lpCode;
    private String tierStatus;
    private int totalPoints;

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

    public String getTierStatus() {
        return tierStatus;
    }

    public void setTierStatus(String tierStatus) {
        this.tierStatus = tierStatus;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return "CustomerProgramStatus{" +
                "walletId='" + walletId + '\'' +
                ", lpCode='" + lpCode + '\'' +
                ", tierStatus='" + tierStatus + '\'' +
                ", totalPoints=" + totalPoints +
                '}';
    }
}
