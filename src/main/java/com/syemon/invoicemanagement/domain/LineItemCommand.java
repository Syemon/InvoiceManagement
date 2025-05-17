package com.syemon.invoicemanagement.domain;

import java.math.BigDecimal;

public record LineItemCommand (
        String description,
        BigDecimal amountPerItem,
        Integer quantity,
        BigDecimal tax
){

}
