package com.syemon.invoicemanagement.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@Data
public class LineItem {
    public static final String MUST_BE_GREATER_THAN_ZERO = "must be greater than zero";
    public static final String MUST_NOT_BE_NEGATIVE = "must not be negative";
    public static final String MUST_BE_NOT_BLANK = "must be not blank";

    private String description;
    private BigDecimal amountPerItem;
    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;
    private Integer quantity;
    private BigDecimal tax;

    public LineItem() {
    }

    public LineItem(String description, BigDecimal amountPerItem, Integer quantity, BigDecimal tax) {
        this.description = description;
        this.amountPerItem = amountPerItem;
        this.quantity = quantity;
        this.tax = tax;
    }

    public LineItem validate() {
        Map<String, String> validationErrors = new HashMap<>();
        if (amountPerItem == null || amountPerItem.compareTo(BigDecimal.ZERO) <= 0) {
            validationErrors.put("amountPerItem", MUST_BE_GREATER_THAN_ZERO);
        }
        if (quantity == null || quantity <= 0) {
            validationErrors.put("quantity", MUST_BE_GREATER_THAN_ZERO);
        }
        if (tax != null && tax.compareTo(BigDecimal.ZERO) < 0) {
            validationErrors.put("tax", MUST_NOT_BE_NEGATIVE);
        }
        if (description == null || description.isBlank()) {
            validationErrors.put("description", MUST_BE_NOT_BLANK);
        }

        if (!validationErrors.isEmpty()) {
            throw new InvalidDomainFieldsException(
                    "Invalid LineItem properties: " + validationErrors,
                    validationErrors
            );
        }
        return this;
    }

    public void calculateTotalAmountAndTotalTaxAmount(Currency currency) {
        Money itemSubTotal = Money.of(amountPerItem, currency);
        Money totalLineItemsAmount = itemSubTotal.applyVAT(tax).multiply(quantity);
        this.totalAmount = totalLineItemsAmount.getAmount();
        this.totalTaxAmount = totalAmount.subtract(itemSubTotal.multiply(quantity).getAmount());
    }
}
