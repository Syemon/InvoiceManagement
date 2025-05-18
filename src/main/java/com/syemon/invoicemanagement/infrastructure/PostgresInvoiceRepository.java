package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PostgresInvoiceRepository {

    public static final PageRequest FIND_INVOICES_TO_GENERATE_LIMIT = PageRequest.of(1, 5);
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

    public List<InvoiceJpaEntity> findInvoicesToGenerate(List<InvoiceStatus> invoiceStatuses) {
        return repository.findInvoiceJpaEntitiesByInvoiceStatusIn(invoiceStatuses, FIND_INVOICES_TO_GENERATE_LIMIT);
    }

}
