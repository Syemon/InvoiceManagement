package com.syemon.invoicemanagement.application.mapper;

import com.syemon.invoicemanagement.application.create.CreateLineItemRequest;
import com.syemon.invoicemanagement.domain.LineItemCommand;

public class LineItemApplicationMapper {
    public LineItemCommand toCommand(CreateLineItemRequest request) {
        return new LineItemCommand(
                request.description(),
                request.amountPerItem(),
                request.quantity(),
                request.tax()
        );
    }
}
