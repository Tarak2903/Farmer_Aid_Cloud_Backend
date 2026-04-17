package com.kisaanconnect.kisaanconnect.dto;

public class NearbyPoolResponse {

    private Long poolId;
    private String cropType;
    private Double targetQuantity;
    private Double currentQuantity;
    private String status;
    private Double distanceKm;

    public NearbyPoolResponse(
            Long poolId,
            String cropType,
            Double targetQuantity,
            Double currentQuantity,
            String status,
            Double distanceKm
    ) {
        this.poolId = poolId;
        this.cropType = cropType;
        this.targetQuantity = targetQuantity;
        this.currentQuantity = currentQuantity;
        this.status = status;
        this.distanceKm = distanceKm;
    }

    public Long getPoolId() {
        return poolId;
    }

    public String getCropType() {
        return cropType;
    }

    public Double getTargetQuantity() {
        return targetQuantity;
    }

    public Double getCurrentQuantity() {
        return currentQuantity;
    }

    public String getStatus() {
        return status;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }
}
