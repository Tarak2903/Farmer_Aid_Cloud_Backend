package com.kisaanconnect.kisaanconnect.dto;

import com.kisaanconnect.kisaanconnect.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreatePoolResponse {
    @NotBlank
    private String cropType;

    @NotNull
    @Positive
    private Double targetQuantity;

    @NotNull
    @Positive
    private Double initialQuantity;

    @NotNull
    private User creator;

    public CreatePoolResponse(String cropType, Double targetQuantity, Double currentQuantity, User createdBy) {
    }


    // getters & setters

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public Double getTargetQuantity() {
        return targetQuantity;
    }

    public void setTargetQuantity(Double targetQuantity) {
        this.targetQuantity = targetQuantity;
    }

    public Double getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Double initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public void setCreator(User creator){
        this.creator=creator;
    }
    public User getCreator(){
        return creator;
    }


}
