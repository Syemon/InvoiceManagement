package com.syemon.invoicemanagement.application;

import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceCommand;
import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.domain.LineItemCommand;
import com.syemon.invoicemanagement.domain.LineItemMapper;
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
