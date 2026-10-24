package com.ganesh.drycleaning.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ServiceInput(@NotBlank @Size(max = 150) String category, @NotBlank @Size(max = 200) String name,
                           @NotBlank @Size(max = 4000) String description,
                           @DecimalMin("0.00") @Digits(integer = 8, fraction = 2) BigDecimal price,
                           @Min(0) int sortOrder) {
}