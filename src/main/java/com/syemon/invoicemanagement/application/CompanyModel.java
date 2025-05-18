package com.syemon.invoicemanagement.application;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanyModel(
        @NotBlank String name,
        @NotBlank String phoneNumber,
        @Email String email,
        @NotNull AddressModel address
) {
}
