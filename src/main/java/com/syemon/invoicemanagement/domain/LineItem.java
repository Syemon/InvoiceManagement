package com.syemon.invoicemanagement.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LineItem {
    private String description;
    private BigDecimal amount;
    private Integer quantity;
    private Integer tax;
}
