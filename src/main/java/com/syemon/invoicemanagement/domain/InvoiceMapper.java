package com.syemon.invoicemanagement.domain;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceMapper {

    private final LineItemMapper lineItemMapper;

    public Invoice toDomain(InvoiceCommand command) {
        Company seller = command.seller();
        Company buyer = command.buyer();

        List<LineItem> lineItems = command.lineItems().stream()
                .map(lineItemMapper::toDomain)
                .collect(Collectors.toList());

        return Invoice.builder()
                .invoiceHeader(command.invoiceHeader())
                .invoiceDate(command.invoiceDate())
                .dueTime(command.dueTime())
                .seller(seller)
                .buyer(buyer)
                .lineItems(lineItems)
                .currency(command.currency())
                .build();

    }
}
