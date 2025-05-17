package com.syemon.invoicemanagement.application;

import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.DocumentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;

public record CreateInvoiceRequest(
    @NotNull DocumentType invoiceHeader,
    @NotNull OffsetDateTime invoiceDate,
    @NotNull OffsetDateTime dueTime,
    @NotNull Company seller,
    @NotNull Company buyer,
    @Validated @NotNull @Size(max = 1000) List<CreateLineItemRequest> lineItems,
    @NotNull Currency currency
){

}
