package com.syemon.invoicemanagement.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LineItemTest {

    public static final String PRODUCT_DESCRIPTION = "Product 1";

    @Test
    void calculateTotalAmountAndTotalTaxAmount() {
        //given
        LineItem sut = new LineItem(
                PRODUCT_DESCRIPTION,
                new BigDecimal("20.99"),
                10,
                new BigDecimal(21));

        //when
        sut.calculateTotalAmountAndTotalTaxAmount(Currency.getInstance("EUR"));

        //then
        assertThat(sut.getTotalAmount()).isEqualTo(new BigDecimal("254.00"));
        assertThat(sut.getTotalTaxAmount()).isEqualTo(new BigDecimal("44.10"));
    }

    @ParameterizedTest
    @MethodSource("calculateTotalAmountAndTotalTaxAmount_shouldThrowExceptionWhenIllegalValueProvider")
    void create_shouldThrowExceptionWhenIllegalValue(
            BigDecimal amountPerItem,
            Integer quantity,
            BigDecimal tax,
            String description,
            Map<String, String> expectedErrors
    ) {
        //given
        LineItem sut = new LineItem(description, amountPerItem, quantity, tax);

        //when/then
        assertThatExceptionOfType(InvalidDomainFieldsException.class)
                .isThrownBy(sut::validate)
                .hasFieldOrPropertyWithValue("errors", expectedErrors);
    }

    public static Stream<Arguments> calculateTotalAmountAndTotalTaxAmount_shouldThrowExceptionWhenIllegalValueProvider() {
        return Stream.of(
                Arguments.of(
                        null,
                        null,
                        null,
                        null,
                        Map.of(
                                "amountPerItem", LineItem.MUST_BE_GREATER_THAN_ZERO,
                                "quantity", LineItem.MUST_BE_GREATER_THAN_ZERO,
                                "description", LineItem.MUST_BE_NOT_BLANK
                        )
                ),
                Arguments.of(
                        BigDecimal.ZERO,
                        null,
                        BigDecimal.ZERO,
                        " ",
                        Map.of("amountPerItem", LineItem.MUST_BE_GREATER_THAN_ZERO,
                                "quantity", LineItem.MUST_BE_GREATER_THAN_ZERO,
                                "description", LineItem.MUST_BE_NOT_BLANK
                        )
                )
        );
    }

}