package com.syemon.invoicemanagement.application;

import jakarta.validation.constraints.NotBlank;

public record AddressModel(
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String postalCode,
        @NotBlank String country
) {
}
