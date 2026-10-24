package com.ganesh.drycleaning.service;

import com.ganesh.drycleaning.dto.PickupInput;
import com.ganesh.drycleaning.entity.*;
import com.ganesh.drycleaning.repository.*;
import com.ganesh.drycleaning.exception.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class PickupService {
    private final PickupRequestRepository repository;
    private final ServiceItemRepository services;
    private final ApplicationEventPublisher events;

    public PickupService(
            PickupRequestRepository repository,
            ServiceItemRepository services,
            ApplicationEventPublisher events) {
        this.repository = repository;
        this.services = services;
        this.events = events;
    }

    public PickupRequest create(PickupInput in) {
        if (services.findAll().stream().noneMatch(s -> s.getName().equals(in.service())))
            throw new IllegalArgumentException("Unknown service");
        PickupRequest p = new PickupRequest();
        p.setName(in.name());
        p.setPhone(in.phone());
        p.setEmail(in.email());
        p.setAddress(in.address());
        p.setService(in.service());
        p.setPreferredDate(in.preferredDate());
        p.setPreferredTime(in.preferredTime());
        p.setNotes(in.notes());
        p.setStatus(PickupStatus.NEW);
        p.setCreatedAt(Instant.now());
        PickupRequest saved = repository.save(p);
        events.publishEvent(new PickupCreatedEvent(saved.getId()));
        return saved;
    }

    public List<PickupRequest> list() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public PickupRequest updateStatus(Long id, PickupStatus status) {
        PickupRequest p = repository.findById(id).orElseThrow(() -> new NotFoundException("Pickup not found"));
        p.setStatus(status);
        return repository.save(p);
    }
}