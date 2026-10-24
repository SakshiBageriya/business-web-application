package com.ganesh.drycleaning.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PickupInput(@NotBlank @Size(max = 100) String name,
                          @NotBlank @Pattern(regexp = "[+0-9() -]{7,25}") String phone,
                          @Email @Size(max = 254) String email, @NotBlank @Size(max = 2000) String address,
                          @NotBlank @Size(max = 200) String service, @NotNull @FutureOrPresent LocalDate preferredDate,
                          @NotBlank @Pattern(regexp = "Morning|Afternoon|Evening") String preferredTime,
                          @Size(max = 2000) String notes) {
}
