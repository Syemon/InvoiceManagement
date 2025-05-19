package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    InvoiceJpaEntity save(InvoiceJpaEntity invoice);
    Optional<Invoice> findByUuid(UUID uuid);
    List<InvoiceJpaEntity> findInvoicesByStatus(InvoiceStatus status, Integer limit);
}
