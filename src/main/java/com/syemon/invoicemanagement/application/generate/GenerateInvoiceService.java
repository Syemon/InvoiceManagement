package com.syemon.invoicemanagement.application.generate;

import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceStatus;
import com.syemon.invoicemanagement.infrastructure.InvoiceInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaEntity;
import com.syemon.invoicemanagement.infrastructure.PostgresInvoiceRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GenerateInvoiceService {

    private final PostgresInvoiceRepository postgresInvoiceRepository;
    private final InvoiceInfrastructureMapper invoiceInfrastructureMapper;

    private static final List<InvoiceStatus> STATUSES_TO_GENERATE = List.of(InvoiceStatus.NEW, InvoiceStatus.RETRY);

    public void generateInvoice() {
        List<InvoiceJpaEntity> invoiceJpaEntities = postgresInvoiceRepository.findInvoicesToGenerate(STATUSES_TO_GENERATE);


        invoiceJpaEntities.forEach(invoiceJpaEntity -> {
            Invoice invoice =  invoiceInfrastructureMapper.toDomain(invoiceJpaEntity);

            invoice.calculateTotalAmountAndTotalTaxAmount();


        });



    }

//    private u
}
