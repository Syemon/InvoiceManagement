package com.syemon.invoicemanagement.application.create;

import com.syemon.invoicemanagement.application.CompanyModel;
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
    @Valid @NotNull CompanyModel seller,
    @Valid @NotNull CompanyModel buyer,
    @Valid @NotNull @Size(min = 1, max = 1000) List<LineItemModel> lineItems,
    @NotNull Currency currency
){

}
