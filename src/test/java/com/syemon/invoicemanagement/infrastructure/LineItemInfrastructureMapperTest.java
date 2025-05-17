package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.LineItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class LineItemInfrastructureMapperTest {

    private static final String ITEM_DESCRIPTION = "Item 1";
    private static final BigDecimal ITEM_AMOUNT_PER_ITEM = BigDecimal.TEN;
    private static final BigDecimal ITEM_TOTAL_AMOUNT = BigDecimal.valueOf(100);
    private static final BigDecimal ITEM_TOTAL_TAX_AMOUNT = BigDecimal.valueOf(10);
    private static final int ITEM_QUANTITY = 2;
    private static final BigDecimal ITEM_TAX = BigDecimal.valueOf(5);

    private final LineItemInfrastructureMapper sut = new LineItemInfrastructureMapper();

    @Test
    void toEntity() {
        //given
        LineItem lineItem = LineItem.builder()
                .description(ITEM_DESCRIPTION)
                .amountPerItem(ITEM_AMOUNT_PER_ITEM)
                .totalAmount(ITEM_TOTAL_AMOUNT)
                .totalTaxAmount(ITEM_TOTAL_TAX_AMOUNT)
                .quantity(ITEM_QUANTITY)
                .tax(ITEM_TAX)
                .build();

        //when
        LineItemJpaEntity entity = sut.toEntity(lineItem);

        //then
        assertThat(entity).isNotNull();
        assertThat(entity.getDescription()).isEqualTo(ITEM_DESCRIPTION);
        assertThat(entity.getAmountPerItem()).isEqualTo(ITEM_AMOUNT_PER_ITEM);
        assertThat(entity.getTotalAmount()).isEqualTo(ITEM_TOTAL_AMOUNT);
        assertThat(entity.getTotalTaxAmount()).isEqualTo(ITEM_TOTAL_TAX_AMOUNT);
        assertThat(entity.getQuantity()).isEqualTo(ITEM_QUANTITY);
        assertThat(entity.getTax()).isEqualTo(ITEM_TAX);
    }

    @Test
    void toDomain() {
        //given
        LineItemJpaEntity entity = new LineItemJpaEntity();
        entity.setDescription(ITEM_DESCRIPTION);
        entity.setAmountPerItem(ITEM_AMOUNT_PER_ITEM);
        entity.setTotalAmount(ITEM_TOTAL_AMOUNT);
        entity.setTotalTaxAmount(ITEM_TOTAL_TAX_AMOUNT);
        entity.setQuantity(ITEM_QUANTITY);
        entity.setTax(ITEM_TAX);

        //when
        LineItem lineItem = sut.toDomain(entity);

        //then
        assertThat(lineItem).isNotNull();
        assertThat(lineItem.getDescription()).isEqualTo(ITEM_DESCRIPTION);
        assertThat(lineItem.getAmountPerItem()).isEqualTo(ITEM_AMOUNT_PER_ITEM);
        assertThat(lineItem.getTotalAmount()).isEqualTo(ITEM_TOTAL_AMOUNT);
        assertThat(lineItem.getTotalTaxAmount()).isEqualTo(ITEM_TOTAL_TAX_AMOUNT);
        assertThat(lineItem.getQuantity()).isEqualTo(ITEM_QUANTITY);
        assertThat(lineItem.getTax()).isEqualTo(ITEM_TAX);
    }
}