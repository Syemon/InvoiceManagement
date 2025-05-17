package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.LineItem;

public class LineItemInfrastructureMapper {

    public LineItemJpaEntity toEntity(LineItem lineItem) {
        if (lineItem == null) {
            return null;
        }

        return new LineItemJpaEntity()
                .setDescription(lineItem.getDescription())
                .setAmountPerItem(lineItem.getAmountPerItem())
                .setTotalAmount(lineItem.getTotalAmount())
                .setTotalTaxAmount(lineItem.getTotalTaxAmount())
                .setQuantity(lineItem.getQuantity())
                .setTax(lineItem.getTax());
    }

    public LineItem toDomain(LineItemJpaEntity lineItemJpaEntity) {
        if (lineItemJpaEntity == null) {
            return null;
        }

        return LineItem.builder()
                .description(lineItemJpaEntity.getDescription())
                .amountPerItem(lineItemJpaEntity.getAmountPerItem())
                .totalAmount(lineItemJpaEntity.getTotalAmount())
                .totalTaxAmount(lineItemJpaEntity.getTotalTaxAmount())
                .quantity(lineItemJpaEntity.getQuantity())
                .tax(lineItemJpaEntity.getTax())
                .build();

    }
}