package com.kisaanconnect.kisaanconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "pool_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"pool_id", "farmer_id"})
        }
)
public class PoolMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pool_id", nullable = false)
    private Pool pool;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Column(nullable = false)
    private Double quantityCommitted;



    @Column(nullable = false)
    private LocalDateTime joinedAt;

    public PoolMember() {}

    @PrePersist
    public void onJoin() {
        this.joinedAt = LocalDateTime.now();

    }


    public Long getId() {
        return id;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public User getFarmer() {
        return farmer;
    }

    public void setFarmer(User farmer) {
        this.farmer = farmer;
    }

    public Double getQuantityCommitted() {
        return quantityCommitted;
    }

    public void setQuantityCommitted(Double quantityCommitted) {
        this.quantityCommitted = quantityCommitted;
    }



    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
