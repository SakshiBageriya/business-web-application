package com.ganesh.drycleaning.dto;

import jakarta.validation.constraints.*;

public record ReadInput(@NotNull Boolean isRead) {
}