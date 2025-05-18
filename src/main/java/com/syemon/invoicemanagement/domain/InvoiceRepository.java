package com.syemon.invoicemanagement.domain;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    Optional<Invoice> findByUuid(UUID uuid);
}
