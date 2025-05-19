package com.syemon.invoicemanagement.application.generate;

import com.syemon.invoicemanagement.application.mapper.InvoiceMapper;
import com.syemon.invoicemanagement.domain.InvoiceStatus;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaEntity;
import com.syemon.invoicemanagement.infrastructure.InvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class GenerateInvoiceService {

    private final InvoiceRepository postgresInvoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public static final List<InvoiceStatus> STATUSES_TO_GENERATE = List.of(InvoiceStatus.NEW, InvoiceStatus.RETRY);
    public static final Integer INVOICE_PER_QUERY_LIMIT = 5;


    public void calculateInvoiceValues() {
        List<InvoiceJpaEntity> invoiceJpaEntities = postgresInvoiceRepository.findInvoicesByStatusIn(STATUSES_TO_GENERATE, INVOICE_PER_QUERY_LIMIT);

        if (invoiceJpaEntities.isEmpty()) {
            log.info("No Invoices to process");
        }

    }
}
