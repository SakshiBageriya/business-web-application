package com.ganesh.drycleaning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ganesh.drycleaning.entity.BusinessSettings;

public interface BusinessSettingsRepository extends JpaRepository<BusinessSettings, Long> {
}