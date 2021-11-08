package com.example.marketplace.model;

public class LoyaltyProgram {
    private String code;
    private String name;
    private String brandId;
    private int isTiered;
    private int isValidated;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public int getIsTiered() {
        return isTiered;
    }

    public void setIsTiered(int isTiered) {
        this.isTiered = isTiered;
    }

    public int getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(int isValidated) {
        this.isValidated = isValidated;
    }

    @Override
    public String toString() {
        return "LoyaltyProgram{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", brandId='" + brandId + '\'' +
                ", isTiered=" + isTiered +
                ", isValidated=" + isValidated +
                '}';
    }
}
