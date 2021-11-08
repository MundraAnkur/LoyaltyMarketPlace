package com.example.marketplace.model;

import java.sql.Date;

public class Brands {
    private String brandId;
    private String brandName;
    private String address;
    private Date joinDate;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "Brands{" +
                "brandId='" + brandId + '\'' +
                ", brandName='" + brandName + '\'' +
                ", address='" + address + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }
}
