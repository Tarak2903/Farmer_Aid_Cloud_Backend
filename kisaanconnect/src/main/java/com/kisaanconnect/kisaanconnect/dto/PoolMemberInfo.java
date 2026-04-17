package com.kisaanconnect.kisaanconnect.dto;

public class PoolMemberInfo {

    private String farmerName;
    private String farmerPhone;
    private Double quantity;

    public PoolMemberInfo(String farmerName, String farmerPhone, Double quantity) {
        this.farmerName = farmerName;
        this.farmerPhone = farmerPhone;
        this.quantity = quantity;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public String getFarmerPhone() {
        return farmerPhone;
    }

    public Double getQuantity() {
        return quantity;
    }
}
