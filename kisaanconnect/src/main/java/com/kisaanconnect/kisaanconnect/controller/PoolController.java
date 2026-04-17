package com.kisaanconnect.kisaanconnect.controller;

import com.kisaanconnect.kisaanconnect.dto.CreatePoolRequest;
import com.kisaanconnect.kisaanconnect.dto.CreatePoolResponse;
import com.kisaanconnect.kisaanconnect.dto.JoinPoolRequest;
import com.kisaanconnect.kisaanconnect.dto.MyPoolsResult;
import com.kisaanconnect.kisaanconnect.entity.Pool;
import com.kisaanconnect.kisaanconnect.entity.User;
import com.kisaanconnect.kisaanconnect.security.SecurityUtil;
import com.kisaanconnect.kisaanconnect.service.PoolService;
import com.kisaanconnect.kisaanconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pools")
public class PoolController {

    private final PoolService poolService;


    public PoolController(PoolService poolService) {
        this.poolService = poolService;
    }

    // ===============================
    // CREATE POOL
    // ===============================
    @PostMapping
    public ResponseEntity<CreatePoolResponse> createPool(
            @RequestBody @Valid CreatePoolRequest request
    ) {

        User creator = SecurityUtil.getCurrentUser();

        CreatePoolResponse pool = poolService.createPool(
               request,
                creator
        );

        return new ResponseEntity<>(pool, HttpStatus.CREATED);
    }



    // ===============================
    // JOIN POOL
    // ===============================
    @PostMapping("/{poolId}/join")
    public ResponseEntity<String> joinPool(
            @PathVariable Long poolId,
            @RequestBody @Valid JoinPoolRequest request
    ) {

        User farmer = SecurityUtil.getCurrentUser();

        poolService.joinPool(poolId, farmer, request.getQuantity());

        return ResponseEntity.ok("Joined pool successfully");
    }




    // ===============================
    // LEAVE POOL
    // ===============================
    @PostMapping("/{poolId}/leave")
    public ResponseEntity<String> leavePool(@PathVariable Long poolId) {

        User farmer = SecurityUtil.getCurrentUser();

        poolService.leavePool(poolId, farmer);

        return ResponseEntity.ok("Left pool successfully");
    }


    // ===============================
    // GET POOL DETAILS
    // ===============================
    @GetMapping("/{poolId}")
    public ResponseEntity<Pool> getPool(@PathVariable Long poolId) {

        Pool pool = poolService.getPoolById(poolId);

        return ResponseEntity.ok(pool);
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyPools() {

        User farmer = SecurityUtil.getCurrentUser();

        return ResponseEntity.ok(
                poolService.getNearbyPools(farmer)
        );
    }
    @GetMapping("/my")
    public ResponseEntity<MyPoolsResult> getMyPools() {
        User user = SecurityUtil.getCurrentUser();

        return ResponseEntity.ok(
                poolService.getMyPools(user)
        );
    }
    @DeleteMapping("/{poolId}")
    public ResponseEntity<String> deletePool(@PathVariable Long poolId) {

        User user = SecurityUtil.getCurrentUser();
        poolService.deletePool(poolId, user);

        return ResponseEntity.ok("Pool deleted successfully");
    }


}


