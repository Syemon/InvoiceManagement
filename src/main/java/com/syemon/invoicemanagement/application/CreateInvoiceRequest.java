package com.syemon.invoicemanagement.application;

import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.DocumentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;

public record CreateInvoiceRequest(
    @NotNull DocumentType invoiceHeader,
    @NotNull OffsetDateTime invoiceDate,
    @NotNull OffsetDateTime dueTime,
    @Valid @NotNull Company seller,
    @Valid @NotNull Company buyer,
    @Valid @NotNull @Size(min = 1, max = 1000) List<CreateLineItemRequest> lineItems,
    @NotNull Currency currency
){

}
