package com.syemon.invoicemanagement.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Getter
public class Invoice {
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
    private final Currency currency;
    private boolean paid = false;

    public Invoice(List<LineItem> lineItems, Company buyer, Company seller, OffsetDateTime dueTime, OffsetDateTime invoiceDate, DocumentType invoiceHeader, Currency currency) {
        this.lineItems = lineItems;
        this.buyer = buyer;
        this.seller = seller;
        this.dueTime = dueTime;
        this.invoiceDate = invoiceDate;
        this.invoiceHeader = invoiceHeader;
        this.currency = currency;
    }

    public Money calculateTotalAmountAndTotalTaxAmount() {
        Money calculatedTotalAmount = Money.of(BigDecimal.ZERO, currency);
        Money calculatedTotalTaxAmount = Money.of(BigDecimal.ZERO, currency);

        for (LineItem lineItem : lineItems) {
            lineItem.calculateTotalAmountAndTotalTaxAmount(currency);
            calculatedTotalAmount = calculatedTotalAmount
                    .add(Money.of(lineItem.getTotalAmount(), currency));
            calculatedTotalTaxAmount = calculatedTotalTaxAmount
                    .add(Money.of(lineItem.getTotalTaxAmount(), currency));

        }

        totalAmount = calculatedTotalAmount;
        totalTaxAmount = calculatedTotalTaxAmount;

        return totalAmount;
    }

}
