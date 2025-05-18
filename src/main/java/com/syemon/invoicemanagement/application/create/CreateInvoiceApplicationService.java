package com.syemon.invoicemanagement.application.create;

import com.syemon.invoicemanagement.application.mapper.InvoiceApplicationMapper;
import com.syemon.invoicemanagement.domain.Owner;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.infrastructure.InvoiceInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaEntity;
import com.syemon.invoicemanagement.infrastructure.OwnerJpaEntity;
import com.syemon.invoicemanagement.infrastructure.OwnerPostgresRepository;
import com.syemon.invoicemanagement.infrastructure.PostgresInvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
public class CreateInvoiceApplicationService {
    public static final String SUCCESS_DESCRIPTION = "Invoice accepted. Will calculate items in up to 30 minutes";
    private final PostgresInvoiceRepository invoiceRepository;
    private final InvoiceApplicationMapper invoiceApplicationMapper;
    private final OwnerPostgresRepository ownerPostgresRepository;
    private final InvoiceInfrastructureMapper invoiceInfrastructureMapper;

    @Transactional
    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest createInvoiceRequest, Owner owner) {
        Invoice invoice = invoiceApplicationMapper.toDomain(createInvoiceRequest);
        invoice.generateId();

        Invoice persistedInvoice = persistEntities(owner, invoice);

        CreateInvoiceResponse response = new CreateInvoiceResponse();
        response.setStatus(HttpStatus.ACCEPTED.value());
        response.setData(invoiceApplicationMapper.toModel(persistedInvoice));
        response.setDescription(SUCCESS_DESCRIPTION);
        return response;
    }

    private Invoice persistEntities(Owner owner, Invoice invoice) {
        InvoiceJpaEntity invoiceJpaEntity = invoiceInfrastructureMapper.toEntity(invoice);
        invoiceJpaEntity.getLineItems().forEach(lineItemJpaEntity -> lineItemJpaEntity.setInvoice(invoiceJpaEntity));
        OwnerJpaEntity ownerJpaEntity = ownerPostgresRepository.findByUsername(owner.getUsername()).get();
        invoiceJpaEntity.setOwner(ownerJpaEntity);
        return invoiceRepository.save(invoiceJpaEntity);
    }
}
