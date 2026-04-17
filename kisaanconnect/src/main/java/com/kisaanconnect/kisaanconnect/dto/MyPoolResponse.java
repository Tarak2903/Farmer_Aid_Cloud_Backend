package com.kisaanconnect.kisaanconnect.dto;

import java.util.List;

public class MyPoolResponse {

    private Long poolId;
    private String cropType;
    private Double targetQuantity;
    private Double currentQuantity;
    private String role;
    private Double myQuantity;
    private String creatorName;
    private String creatorPhone;

    // ✅ NEW
    private List<PoolMemberInfo> members;

    public MyPoolResponse(
            Long poolId,
            String cropType,
            Double targetQuantity,
            Double currentQuantity,
            String role,
            Double myQuantity,
            String creatorName,
            String creatorPhone,
            List<PoolMemberInfo> members
    ) {
        this.poolId = poolId;
        this.cropType = cropType;
        this.targetQuantity = targetQuantity;
        this.currentQuantity = currentQuantity;
        this.role = role;
        this.myQuantity = myQuantity;
        this.creatorName = creatorName;
        this.creatorPhone = creatorPhone;
        this.members = members;
    }

    public Long getPoolId() { return poolId; }
    public String getCropType() { return cropType; }
    public Double getTargetQuantity() { return targetQuantity; }
    public Double getCurrentQuantity() { return currentQuantity; }
    public String getRole() { return role; }
    public Double getMyQuantity() { return myQuantity; }
    public String getCreatorName() { return creatorName; }
    public String getCreatorPhone() { return creatorPhone; }


    public List<PoolMemberInfo> getMembers() {
        return members;
    }
}
