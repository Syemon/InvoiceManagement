package com.syemon.invoicemanagement.domain;

public record Company(
        String name,
        String phoneNumber,
        String email,
        Address address
) {
}
