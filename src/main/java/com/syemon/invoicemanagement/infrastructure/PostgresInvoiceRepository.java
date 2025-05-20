package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PostgresInvoiceRepository implements InvoiceRepository {

    private final InvoiceJpaRepository repository;

    public InvoiceJpaEntity save(InvoiceJpaEntity invoiceJpaEntity) {
        return repository.save(invoiceJpaEntity);
    }

    @Transactional
    public Optional<InvoiceJpaEntity> findByUuid(UUID uuid) {
        return repository.findByUuid(uuid);
    }

    public List<InvoiceJpaEntity> findInvoicesByStatusIn(List<InvoiceStatus> status, Integer limit) {
        return repository.findInvoicesByStatusIn(status, limit);
    }

}
