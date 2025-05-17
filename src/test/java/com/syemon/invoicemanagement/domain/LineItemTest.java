package com.syemon.invoicemanagement.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class LineItemTest {

    public static final String PRODUCT_DESCRIPTION = "Product 1";

    @Test
    void calculateTotalAmountAndTotalTaxAmount() {
        //given
        LineItem sut = LineItem.builder()
                .amountPerItem(new BigDecimal("20.99"))
                .quantity(10)
                .tax(new BigDecimal(21))
                .description(PRODUCT_DESCRIPTION)
                .build();

        //when
        sut.calculateTotalAmountAndTotalTaxAmount(Currency.getInstance("EUR"));

        //then
        assertThat(sut.getTotalAmount()).isEqualTo(new BigDecimal("254.00"));
        assertThat(sut.getTotalTaxAmount()).isEqualTo(new BigDecimal("44.10"));
    }

}