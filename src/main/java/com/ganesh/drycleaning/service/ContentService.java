package com.ganesh.drycleaning.service;

import com.ganesh.drycleaning.dto.*;
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
public class ContentService {
    private final ServiceItemRepository items;
    private final BusinessSettingsRepository settings;
    private final ContactMessageRepository messages;
    private final ApplicationEventPublisher events;

    public ContentService(ServiceItemRepository items, BusinessSettingsRepository settings, ContactMessageRepository messages, ApplicationEventPublisher events) {
        this.items = items;
        this.settings = settings;
        this.messages = messages;
        this.events = events;
    }

    public List<ServiceItem> services() {
        return items.findAllByOrderBySortOrderAscIdAsc();
    }

    public BusinessSettings settings() {
        return settings.findById(1L).orElseThrow(() -> new NotFoundException("Settings not initialized"));
    }

    public ServiceItem saveService(Long id, ServiceInput in) {
        ServiceItem s = id == null ? new ServiceItem() : items.findById(id).orElseThrow(() -> new NotFoundException("Service not found"));
        s.setCategory(in.category());
        s.setName(in.name());
        s.setDescription(in.description());
        s.setPrice(in.price());
        s.setSortOrder(in.sortOrder());
        return items.save(s);
    }

    public void deleteService(Long id) {
        if (!items.existsById(id)) throw new NotFoundException("Service not found");
        items.deleteById(id);
    }

    public BusinessSettings saveSettings(SettingsInput in) {
        BusinessSettings s = settings();
        s.setPhone(in.phone());
        s.setWhatsapp(in.whatsapp());
        s.setEmail(in.email());
        s.setHours(in.hours());
        s.setBranches(in.branches());
        s.setMapsUrl(in.mapsUrl());
        s.setInstagram(in.instagram());
        s.setFacebook(in.facebook());
        s.setExperience(in.experience());
        s.setCareClaim(in.careClaim());
        s.setFabricClaim(in.fabricClaim());
        s.setCustomerClaim(in.customerClaim());
        return settings.save(s);
    }

    public ContactMessage contact(ContactInput in) {
        ContactMessage m = new ContactMessage();
        m.setName(in.name());
        m.setPhone(in.phone());
        m.setEmail(in.email());
        m.setMessage(in.message());
        m.setIsRead(false);
        m.setCreatedAt(Instant.now());
        ContactMessage saved = messages.save(m);
        events.publishEvent(new ContactCreatedEvent(saved.getId()));
        return saved;
    }

    public List<ContactMessage> messages() {
        return messages.findAllByOrderByCreatedAtDesc();
    }

    public ContactMessage markRead(Long id, boolean read) {
        ContactMessage m = messages.findById(id).orElseThrow(() -> new NotFoundException("Message not found"));
        m.setIsRead(read);
        return messages.save(m);
    }
}