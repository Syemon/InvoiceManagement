package com.syemon.invoicemanagement.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public class LineItem {
    private String description;
    private BigDecimal amountPerItem;
    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;
    private Integer quantity;
    private BigDecimal tax;

    public void calculateTotalAmountAndTotalTaxAmount(Currency currency) {
        Money itemSubTotal = Money.of(amountPerItem, currency);
        Money totalLineItemsAmount = itemSubTotal.applyVAT(tax).multiply(quantity);
        this.totalAmount = totalLineItemsAmount.getAmount();
        this.totalTaxAmount = totalAmount.subtract(itemSubTotal.multiply(quantity).getAmount());
    }
}
