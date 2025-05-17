package com.syemon.invoicemanagement.domain;

public class LineItemMapper {
    public LineItem toDomain(LineItemCommand command) {
        return LineItem.builder()
                .description(command.description())
                .amountPerItem(command.amountPerItem())
                .quantity(command.quantity())
                .tax(command.tax())
                .build();
    }
}
