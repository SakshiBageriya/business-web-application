package com.ganesh.drycleaning.dto;

import jakarta.validation.constraints.*;

public record ContactInput(@NotBlank @Size(max = 100) String name,
                           @NotBlank @Pattern(regexp = "[+0-9() -]{7,25}") String phone,
                           @Email @Size(max = 254) String email, @NotBlank @Size(max = 4000) String message) {
}