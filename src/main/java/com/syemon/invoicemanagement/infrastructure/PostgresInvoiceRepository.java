package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Invoice;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PostgresInvoiceRepository {

    private final InvoiceJpaRepository repository;
    private final InvoiceInfrastructureMapper invoiceInfrastructureMapper;

    public Invoice save(InvoiceJpaEntity invoiceJpaEntity) {
        InvoiceJpaEntity persistedEntity = repository.save(invoiceJpaEntity);
        return invoiceInfrastructureMapper.toDomain(persistedEntity);
    }

    @Transactional
    public Optional<Invoice> findByUuid(UUID uuid) {
        Optional<InvoiceJpaEntity> optionalEntity = repository.findByUuid(uuid);
        return optionalEntity.map(invoiceInfrastructureMapper::toDomain);
    }
}
