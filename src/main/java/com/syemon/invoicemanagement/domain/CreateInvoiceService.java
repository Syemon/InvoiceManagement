package com.syemon.invoicemanagement.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CreateInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public Invoice createInvoice(InvoiceCommand invoiceCommand) {
        Invoice invoice = invoiceMapper.toDomain(invoiceCommand);
        UUID uuid = invoice.generateId();
        log.info("Invoice {} created", uuid);
        Invoice persistedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice {} persisted successfully", uuid);
        return persistedInvoice;
    }

}
