package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.Owner;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    Owner save(InvoiceJpaEntity invoice);
    Optional<Invoice> findByUuid(UUID uuid);
}
