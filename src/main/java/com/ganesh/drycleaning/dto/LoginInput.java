package com.ganesh.drycleaning.dto;

import jakarta.validation.constraints.*;

public record LoginInput(
        @NotBlank @Size(max = 100) String username,
        @NotBlank @Size(max = 200) String password
) {

}