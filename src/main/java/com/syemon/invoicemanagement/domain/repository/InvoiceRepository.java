package com.syemon.invoicemanagement.domain.repository;

import com.syemon.invoicemanagement.domain.Invoice;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    Optional<Invoice> findByUuid(UUID uuid);
}
