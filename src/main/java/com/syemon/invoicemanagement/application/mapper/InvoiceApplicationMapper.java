package com.syemon.invoicemanagement.application.mapper;

import com.syemon.invoicemanagement.application.create.CreateInvoiceRequest;
import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceCommand;
import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.domain.LineItemCommand;
import com.syemon.invoicemanagement.domain.mapper.LineItemMapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceApplicationMapper {

    private final LineItemApplicationMapper lineItemMapper;

    public Invoice toDomain(CreateInvoiceRequest command) {
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
