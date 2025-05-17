package com.syemon.invoicemanagement.application;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record CreateLineItemRequest(
        @NotNull String description,
        @NotNull @Range(min = 1, max = 9999999) BigDecimal amountPerItem,
        @NotNull @Range(min = 1, max = 9999) Integer quantity,
        @NotNull @Range(min = 0, max = 1000) BigDecimal tax
){

}
