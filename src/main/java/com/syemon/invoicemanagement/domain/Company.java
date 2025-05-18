package com.syemon.invoicemanagement.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Company(
        @NotBlank String name,
        @NotBlank String phoneNumber,
        String email,
        @Valid @NotNull Address address
) {
}
