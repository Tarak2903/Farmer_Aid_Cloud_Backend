package com.kisaanconnect.kisaanconnect.repository;

import com.kisaanconnect.kisaanconnect.entity.Pool;
import com.kisaanconnect.kisaanconnect.entity.PoolMember;
import com.kisaanconnect.kisaanconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PoolMemberRepository extends JpaRepository<PoolMember, Long> {

    boolean existsByPoolAndFarmer(Pool pool, User farmer);

    List<PoolMember> findByPool(Pool pool);

    List<PoolMember> findByFarmerId(Long farmerId);


    Optional<PoolMember> findByPoolAndFarmer(Pool pool, User farmer);

}
