package com.syemon.invoicemanagement.domain;

public record Address(
        String street,
        String city,
        String postalCode,
        String country
) {
}
