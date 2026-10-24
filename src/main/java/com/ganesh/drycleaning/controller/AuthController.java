package com.ganesh.drycleaning.controller;

import com.ganesh.drycleaning.dto.LoginInput;
import com.ganesh.drycleaning.repository.AdminUserRepository;
import com.ganesh.drycleaning.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AdminUserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(AdminUserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginInput in) {
        var u = users.findByUsername(in.username());
        if (u.isEmpty() || !encoder.matches(in.password(), u.get().getPasswordHash()))
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
        return ResponseEntity.ok(Map.of("token", jwt.issue(in.username()), "tokenType", "Bearer"));
    }
}