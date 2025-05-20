package com.syemon.invoicemanagement.application.create;

import com.syemon.invoicemanagement.application.mapper.InvoiceMapper;
import com.syemon.invoicemanagement.domain.InvoiceStatus;
import com.syemon.invoicemanagement.domain.Owner;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaEntity;
import com.syemon.invoicemanagement.infrastructure.InvoiceRepository;
import com.syemon.invoicemanagement.infrastructure.OwnerJpaEntity;
import com.syemon.invoicemanagement.infrastructure.OwnerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
public class CreateInvoiceApplicationService {
    public static final String SUCCESS_DESCRIPTION = "Invoice accepted. Will calculate items in up to 30 minutes";

    private final InvoiceRepository invoiceRepository;
    private final OwnerRepository ownerPostgresRepository;
    private final InvoiceMapper invoiceMapper;

    @Transactional
    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest createInvoiceRequest, Owner owner) {
        InvoiceJpaEntity invoiceJpaEntity = invoiceMapper.toEntity(createInvoiceRequest);
        invoiceJpaEntity.setInvoiceStatus(InvoiceStatus.NEW);

        InvoiceJpaEntity persistedInvoice = persistEntities(owner, invoiceJpaEntity);

        return getCreateInvoiceResponse(persistedInvoice);
    }

    private CreateInvoiceResponse getCreateInvoiceResponse(InvoiceJpaEntity persistedInvoice) {
        CreateInvoiceResponse response = new CreateInvoiceResponse();
        response.setStatus(HttpStatus.ACCEPTED.value());
        response.setData(invoiceMapper.toModel(persistedInvoice));
        response.setDescription(SUCCESS_DESCRIPTION);
        return response;
    }

    private InvoiceJpaEntity persistEntities(Owner owner, InvoiceJpaEntity invoice) {
        invoice.getLineItems().forEach(lineItemJpaEntity -> lineItemJpaEntity.setInvoice(invoice));
        OwnerJpaEntity ownerJpaEntity = ownerPostgresRepository.findByUsername(owner.getUsername()).get();
        invoice.setOwner(ownerJpaEntity);
        return invoiceRepository.save(invoice);
    }
}
