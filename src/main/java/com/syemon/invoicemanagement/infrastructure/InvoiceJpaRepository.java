package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.InvoiceStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceJpaRepository extends JpaRepository<InvoiceJpaEntity, Long> {

    Optional<InvoiceJpaEntity> findByUuid(UUID uuid);

    @Query(name = InvoiceJpaEntity.FIND_INVOICES_BY_STATUS)
    List<InvoiceJpaEntity> findInvoicesByStatus(InvoiceStatus status, Integer limit);
}
