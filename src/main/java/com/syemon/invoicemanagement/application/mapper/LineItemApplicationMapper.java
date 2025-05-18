package com.syemon.invoicemanagement.application.mapper;

import com.syemon.invoicemanagement.application.create.CreateLineItemRequest;
import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.domain.LineItemCommand;

public class LineItemApplicationMapper {
    public LineItem toDomain(CreateLineItemRequest command) {
        return LineItem.builder()
                .description(command.description())
                .amountPerItem(command.amountPerItem())
                .quantity(command.quantity())
                .tax(command.tax())
                .build();
    }
}
