package com.syemon.invoicemanagement.domain;

import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String postalCode,
        @NotBlank String country
) {
}
