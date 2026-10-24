package com.ganesh.drycleaning.controller;

import com.ganesh.drycleaning.service.*;
import com.ganesh.drycleaning.entity.*;
import com.ganesh.drycleaning.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PublicController {
    private final ContentService content;
    private final PickupService pickups;

    public PublicController(ContentService content, PickupService pickups) {
        this.content = content;
        this.pickups = pickups;
    }

    @GetMapping("/services")
    public List<ServiceItem> services() {
        return content.services();
    }

    @GetMapping("/settings")
    public BusinessSettings settings() {
        return content.settings();
    }

    @PostMapping("/pickups")
    @ResponseStatus(HttpStatus.CREATED)
    public PickupRequest pickup(@Valid @RequestBody PickupInput in) {
        return pickups.create(in);
    }

    @PostMapping("/contact")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactMessage contact(@Valid @RequestBody ContactInput in) {
        return content.contact(in);
    }
}