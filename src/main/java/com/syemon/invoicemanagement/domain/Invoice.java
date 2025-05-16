package com.syemon.invoicemanagement.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Invoice {
    private UUID uuid;
    private DocumentType invoiceHeader;
    private OffsetDateTime invoiceDate;
    private OffsetDateTime dueTime;
    private Company seller;
    private Company buyer;
    private List<LineItem> lineItems;
    private Money totalAmount;
    private String paymentLink;
    private boolean paid = false;
}
