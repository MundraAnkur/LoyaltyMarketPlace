package com.example.marketplace.model;

/**
 * @author ankurmundra
 * November 03, 2021
 */
public class EnrollCustomer {
    private String lpCode;
    private String customerId;

    public String getLpCode() {
        return lpCode;
    }

    public void setLpCode(String lpCode) {
        this.lpCode = lpCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "EnrollCustomer{" +
                "lpCode='" + lpCode + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
