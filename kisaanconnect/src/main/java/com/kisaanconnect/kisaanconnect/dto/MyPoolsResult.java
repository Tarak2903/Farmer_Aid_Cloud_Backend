package com.kisaanconnect.kisaanconnect.dto;

import java.util.List;

public class MyPoolsResult {

    private List<MyPoolResponse> createdPools;
    private List<MyPoolResponse> joinedPools;

    public MyPoolsResult(
            List<MyPoolResponse> createdPools,
            List<MyPoolResponse> joinedPools
    ) {
        this.createdPools = createdPools;
        this.joinedPools = joinedPools;
    }

    public List<MyPoolResponse> getCreatedPools() {
        return createdPools;
    }

    public List<MyPoolResponse> getJoinedPools() {
        return joinedPools;
    }
}
