package com.ganesh.drycleaning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ganesh.drycleaning.entity.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    java.util.List<ContactMessage> findAllByOrderByCreatedAtDesc();
}