package com.kisaanconnect.kisaanconnect.service;

import com.kisaanconnect.kisaanconnect.dto.*;
import com.kisaanconnect.kisaanconnect.entity.Pool;
import com.kisaanconnect.kisaanconnect.entity.PoolMember;
import com.kisaanconnect.kisaanconnect.entity.User;
import com.kisaanconnect.kisaanconnect.exception.BusinessException;
import com.kisaanconnect.kisaanconnect.exception.ResourceNotFoundException;
import com.kisaanconnect.kisaanconnect.repository.PoolMemberRepository;
import com.kisaanconnect.kisaanconnect.repository.PoolRepository;
import com.kisaanconnect.kisaanconnect.util.DistanceUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PoolService {

    private final PoolRepository poolRepository;
    private final PoolMemberRepository poolMemberRepository;

    public PoolService(PoolRepository poolRepository,
                       PoolMemberRepository poolMemberRepository) {
        this.poolRepository = poolRepository;
        this.poolMemberRepository = poolMemberRepository;
    }

    // ------------------------------------------------

    // CREATE POOL

    // ------------------------------------------------
    @Transactional
    public CreatePoolResponse createPool(CreatePoolRequest request,
                           User creator) {

        if (request.getTargetQuantity() <= 0 || request.getInitialQuantity() <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        if (request.getInitialQuantity() > request.getTargetQuantity()) {
            throw new BusinessException("Initial quantity cannot exceed target quantity");
        }

        Pool pool = new Pool();
        pool.setCropType(request.getCropType());
        pool.setTargetQuantity(request.getTargetQuantity());
        pool.setCurrentQuantity(request.getInitialQuantity());
        pool.setCreatedBy(creator);

        Pool savedPool = poolRepository.save(pool);

        PoolMember member = new PoolMember();
        member.setPool(savedPool);
        member.setFarmer(creator);
        member.setQuantityCommitted(request.getInitialQuantity());

        poolMemberRepository.save(member);

        return new CreatePoolResponse(
                savedPool.getCropType(),
                savedPool.getTargetQuantity(),
                savedPool.getCurrentQuantity(),
                savedPool.getCreatedBy()
        );
    }

    // ------------------------------------------------
    // JOIN POOL
    // ------------------------------------------------
    @Transactional
    public void joinPool(Long poolId, User farmer, Double quantity) {

        Pool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new ResourceNotFoundException("Pool not found"));

        if (!"OPEN".equals(pool.getStatus())) {
            throw new BusinessException("Pool is not open for joining");
        }

        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        if (poolMemberRepository.existsByPoolAndFarmer(pool, farmer)) {
            throw new BusinessException("Farmer already joined this pool");
        }

        double newQuantity = pool.getCurrentQuantity() + quantity;

        if (newQuantity > pool.getTargetQuantity()) {
            throw new BusinessException("Joining exceeds target quantity");
        }

        PoolMember member = new PoolMember();
        member.setPool(pool);
        member.setFarmer(farmer);
        member.setQuantityCommitted(quantity);

        poolMemberRepository.save(member);

        pool.setCurrentQuantity(newQuantity);
        poolRepository.save(pool);
    }

    // ------------------------------------------------
    // LEAVE POOL  ✅ FIXED
    // ------------------------------------------------
    @Transactional
    public void leavePool(Long poolId, User farmer) {

        Pool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new ResourceNotFoundException("Pool not found"));

        PoolMember member = poolMemberRepository
                .findByPoolAndFarmer(pool, farmer)
                .orElseThrow(() -> new BusinessException("Farmer is not part of this pool"));

        if (!"OPEN".equals(pool.getStatus())) {
            throw new BusinessException("Cannot leave pool after it is closed or sold");
        }

        double updatedQuantity =
                pool.getCurrentQuantity() - member.getQuantityCommitted();

        if (updatedQuantity < 0) {
            throw new BusinessException("Pool quantity corrupted");
        }

        pool.setCurrentQuantity(updatedQuantity);

        poolMemberRepository.delete(member);
        poolRepository.save(pool);
    }

    // ------------------------------------------------
    // GET POOL
    // ------------------------------------------------
    public Pool getPoolById(Long poolId) {
        return poolRepository.findById(poolId)
                .orElseThrow(() -> new ResourceNotFoundException("Pool not found"));
    }

    // ------------------------------------------------
    // NEARBY POOLS
    // ------------------------------------------------
    public List<NearbyPoolResponse> getNearbyPools(User farmer) {

        double FIXED_RADIUS_KM = 10.0;
        List<Pool> openPools = poolRepository.findByStatus("OPEN");

        return openPools.stream()
                .filter(pool -> !pool.getCreatedBy().getId().equals(farmer.getId()))
                .map(pool -> {
                    User creator = pool.getCreatedBy();

                    double distance = DistanceUtil.distanceKm(
                            farmer.getLatitude(),
                            farmer.getLongitude(),
                            creator.getLatitude(),
                            creator.getLongitude()
                    );

                    if (distance <= FIXED_RADIUS_KM) {
                        return new NearbyPoolResponse(
                                pool.getId(),
                                pool.getCropType(),
                                pool.getTargetQuantity(),
                                pool.getCurrentQuantity(),
                                pool.getStatus(),
                                Math.round(distance * 100.0) / 100.0
                        );
                    }
                    return null;
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------
    // MY POOLS
    // ------------------------------------------------
    public MyPoolsResult getMyPools(User user) {

        // ---------------- CREATED POOLS ----------------
        List<MyPoolResponse> createdPools =
                poolRepository.findByCreatedById(user.getId())
                        .stream()
                        .map(pool -> {

                            List<PoolMemberInfo> members =
                                    poolMemberRepository.findByPool(pool)
                                            .stream()
                                            .map(pm -> new PoolMemberInfo(
                                                    pm.getFarmer().getName(),
                                                    pm.getFarmer().getPhoneNumber(),
                                                    pm.getQuantityCommitted()
                                            ))
                                            .toList();

                            return new MyPoolResponse(
                                    pool.getId(),
                                    pool.getCropType(),
                                    pool.getTargetQuantity(),
                                    pool.getCurrentQuantity(),
                                    "CREATED",
                                    null,
                                    user.getName(),
                                    user.getPhoneNumber(),
                                    members
                            );
                        })
                        .toList();

        // ---------------- JOINED POOLS ----------------
        List<MyPoolResponse> joinedPools =
                poolMemberRepository.findByFarmerId(user.getId())
                        .stream()
                        .filter(pm -> !pm.getPool().getCreatedBy().getId().equals(user.getId()))
                        .map(pm -> new MyPoolResponse(
                                pm.getPool().getId(),
                                pm.getPool().getCropType(),
                                pm.getPool().getTargetQuantity(),
                                pm.getPool().getCurrentQuantity(),
                                "JOINED",
                                pm.getQuantityCommitted(),
                                pm.getPool().getCreatedBy().getName(),
                                pm.getPool().getCreatedBy().getPhoneNumber(),
                                null
                        ))
                        .toList();

        return new MyPoolsResult(createdPools, joinedPools);
    }


    // ------------------------------------------------
    // DELETE POOL
    // ------------------------------------------------
    @Transactional
    public void deletePool(Long poolId, User user) {

        Pool pool = poolRepository.findById(poolId)
                .orElseThrow(() -> new ResourceNotFoundException("Pool not found"));

        if (!pool.getCreatedBy().getId().equals(user.getId())) {
            throw new BusinessException("Only pool creator can delete the pool");
        }

        poolMemberRepository.deleteAll(
                poolMemberRepository.findByPool(pool)
        );

        poolRepository.delete(pool);
    }
}
