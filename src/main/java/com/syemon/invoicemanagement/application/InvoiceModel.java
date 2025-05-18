package com.syemon.invoicemanagement.application;

import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.DocumentType;
import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.domain.Money;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class InvoiceModel {
    private UUID uuid;
    private DocumentType invoiceHeader;
    private OffsetDateTime invoiceDate;
    private OffsetDateTime dueTime;
    private Company seller;
    private Company buyer;
    private List<LineItem> lineItems;
    private Money totalAmount;
    private Money totalTaxAmount;
    private String paymentLink;
    private Currency currency;
    private boolean paid;
}
