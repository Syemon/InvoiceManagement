package com.syemon.invoicemanagement.domain.service;

import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CreateInvoiceService {

    private final InvoiceRepository invoiceRepository;

    public Invoice save(Invoice invoice) {
        UUID uuid = invoice.generateId();
        log.info("Invoice {} created", uuid);
        Invoice persistedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice {} persisted successfully", uuid);
        return persistedInvoice;
    }

}
