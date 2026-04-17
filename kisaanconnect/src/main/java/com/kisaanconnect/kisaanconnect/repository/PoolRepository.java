package com.kisaanconnect.kisaanconnect.repository;

import com.kisaanconnect.kisaanconnect.entity.Pool;
import com.kisaanconnect.kisaanconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoolRepository extends JpaRepository<Pool, Long> {

    List<Pool> findByStatus(String status);
    List<Pool> findByCreatedById(Long userId);


}
