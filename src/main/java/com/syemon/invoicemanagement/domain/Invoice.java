package com.syemon.invoicemanagement.domain;

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
public class Invoice {
    private UUID uuid;
    private DocumentType invoiceHeader;
    private OffsetDateTime invoiceDate;
    private OffsetDateTime dueTime;
    private Company seller;
    private Company buyer;
    private List<LineItem> lineItems = new ArrayList<>();
    private Money totalAmount;
    private Money totalTaxAmount;
    private String paymentLink;
    private Currency currency;
    private boolean paid = false;
//    private User systemUser;

    public Invoice() {
    }

    public Invoice(List<LineItem> lineItems, Company buyer, Company seller, OffsetDateTime dueTime, OffsetDateTime invoiceDate, DocumentType invoiceHeader, Currency currency) {
        this.lineItems = lineItems;
        this.buyer = buyer;
        this.seller = seller;
        this.dueTime = dueTime;
        this.invoiceDate = invoiceDate;
        this.invoiceHeader = invoiceHeader;
        this.currency = currency;
    }

    public Invoice(UUID uuid, DocumentType invoiceHeader, OffsetDateTime invoiceDate, OffsetDateTime dueTime, Company seller, Company buyer, List<LineItem> lineItems, Money totalAmount, Money totalTaxAmount, String paymentLink, Currency currency, boolean paid) {
        this.uuid = uuid;
        this.invoiceHeader = invoiceHeader;
        this.invoiceDate = invoiceDate;
        this.dueTime = dueTime;
        this.seller = seller;
        this.buyer = buyer;
        this.lineItems = lineItems;
        this.totalAmount = totalAmount;
        this.totalTaxAmount = totalTaxAmount;
        this.paymentLink = paymentLink;
        this.currency = currency;
        this.paid = paid;
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

    public UUID generateId() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    public List<LineItem> addLineItems(List<LineItem> lineItems) {
        this.lineItems.addAll(lineItems);
        return this.lineItems;
    }

}
