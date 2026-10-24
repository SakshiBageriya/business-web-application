package com.ganesh.drycleaning.dto;

import jakarta.validation.constraints.*;

public record SettingsInput(@NotBlank @Size(max = 255) String phone, @NotBlank @Size(max = 255) String whatsapp,
                            @NotBlank @Size(max = 255) String email, @NotBlank @Size(max = 4000) String hours,
                            @NotBlank @Size(max = 4000) String branches, @Size(max = 255) String mapsUrl,
                            @Size(max = 255) String instagram, @Size(max = 255) String facebook,
                            @NotBlank @Size(max = 255) String experience, @NotBlank @Size(max = 255) String careClaim,
                            @NotBlank @Size(max = 255) String fabricClaim,
                            @NotBlank @Size(max = 255) String customerClaim) {
}