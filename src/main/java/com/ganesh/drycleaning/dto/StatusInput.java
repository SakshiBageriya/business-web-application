package com.ganesh.drycleaning.dto;

import jakarta.validation.constraints.*;
import com.ganesh.drycleaning.entity.PickupStatus;

public record StatusInput(@NotNull PickupStatus status) {
}