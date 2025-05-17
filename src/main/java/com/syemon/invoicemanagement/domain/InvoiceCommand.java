package com.syemon.invoicemanagement.domain;

import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;

public record InvoiceCommand (
    DocumentType invoiceHeader,
    OffsetDateTime invoiceDate,
    OffsetDateTime dueTime,
    Company seller,
    Company buyer,
    List<LineItemCommand> lineItems,
    Currency currency
){
}
