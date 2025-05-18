package com.syemon.invoicemanagement.domain.mapper;

import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.domain.LineItemCommand;

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
