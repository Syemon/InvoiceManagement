package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PostgresInvoiceRepository implements InvoiceRepository {

    private final InvoiceJpaRepository repository;
    private final InvoiceInfrastructureMapper invoiceInfrastructureMapper;

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceJpaEntity entity = invoiceInfrastructureMapper.toEntity(invoice);
        InvoiceJpaEntity persistedEntity = repository.save(entity);
        return  invoiceInfrastructureMapper.toDomain(persistedEntity);
    }
}
