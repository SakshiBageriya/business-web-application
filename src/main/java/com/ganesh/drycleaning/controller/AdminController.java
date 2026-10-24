package com.ganesh.drycleaning.controller;

import com.ganesh.drycleaning.service.*;
import com.ganesh.drycleaning.entity.*;
import com.ganesh.drycleaning.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final ContentService content;
    private final PickupService pickups;

    public AdminController(ContentService content, PickupService pickups) {
        this.content = content;
        this.pickups = pickups;
    }

    @GetMapping("/pickups")
    public List<PickupRequest> pickups() {
        return pickups.list();
    }

    @PatchMapping("/pickups/{id}")
    public PickupRequest status(@PathVariable Long id, @Valid @RequestBody StatusInput in) {
        return pickups.updateStatus(id, in.status());
    }

    @GetMapping("/messages")
    public List<ContactMessage> messages() {
        return content.messages();
    }

    @PatchMapping("/messages/{id}")
    public ContactMessage read(@PathVariable Long id, @Valid @RequestBody ReadInput in) {
        return content.markRead(id, in.isRead());
    }

    @PutMapping("/settings")
    public BusinessSettings settings(@Valid @RequestBody SettingsInput in) {
        return content.saveSettings(in);
    }

    @PostMapping("/services")

    @ResponseStatus(HttpStatus.CREATED)
    public ServiceItem add(@Valid @RequestBody ServiceInput in) {
        return content.saveService(null, in);
    }

    @PutMapping("/services/{id}")
    public ServiceItem update(@PathVariable Long id, @Valid @RequestBody ServiceInput in) {
        return content.saveService(id, in);
    }

    @DeleteMapping("/services/{id}")

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        content.deleteService(id);
    }
}