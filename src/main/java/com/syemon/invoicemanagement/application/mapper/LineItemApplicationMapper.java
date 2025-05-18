package com.syemon.invoicemanagement.application.mapper;

import com.syemon.invoicemanagement.application.create.LineItemModel;
import com.syemon.invoicemanagement.domain.LineItem;

public class LineItemApplicationMapper {
    public LineItem toDomain(LineItemModel command) {
        return LineItem.builder()
                .description(command.description())
                .amountPerItem(command.amountPerItem())
                .quantity(command.quantity())
                .tax(command.tax())
                .build();
    }
}
