package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.InvoiceStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    InvoiceJpaEntity save(InvoiceJpaEntity invoice);
    Optional<InvoiceJpaEntity> findByUuid(UUID uuid);
    List<InvoiceJpaEntity> findInvoicesByStatusIn(List<InvoiceStatus> status, Integer limit);
}
