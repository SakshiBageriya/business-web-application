package com.ganesh.drycleaning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ganesh.drycleaning.entity.ServiceItem;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    java.util.List<ServiceItem> findAllByOrderBySortOrderAscIdAsc();
}