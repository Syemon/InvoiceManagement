package com.syemon.invoicemanagement.application.mapper;

import com.syemon.invoicemanagement.application.create.CreateInvoiceRequest;
import com.syemon.invoicemanagement.domain.InvoiceCommand;
import com.syemon.invoicemanagement.domain.LineItemCommand;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceApplicationMapper {

    private final LineItemApplicationMapper lineItemMapper;

    public InvoiceCommand toCommand(CreateInvoiceRequest request) {
        List<LineItemCommand> lineItemCommands = request.lineItems().stream()
                .map(lineItemMapper::toCommand)
                .collect(Collectors.toList());

        return new InvoiceCommand(
                request.invoiceHeader(),
                request.invoiceDate(),
                request.dueTime(),
                request.seller(),
                request.buyer(),
                lineItemCommands,
                request.currency()
        );
    }
}
