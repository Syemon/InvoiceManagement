package com.syemon.invoicemanagement.application;

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
