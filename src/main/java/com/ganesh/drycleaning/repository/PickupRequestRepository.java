package com.ganesh.drycleaning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ganesh.drycleaning.entity.PickupRequest;

public interface PickupRequestRepository extends JpaRepository<PickupRequest, Long> {
    java.util.List<PickupRequest> findAllByOrderByCreatedAtDesc();
}