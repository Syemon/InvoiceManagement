package com.syemon.invoicemanagement.configuration;

import com.syemon.invoicemanagement.application.create.CreateInvoiceApplicationService;
import com.syemon.invoicemanagement.application.mapper.InvoiceMapper;
import com.syemon.invoicemanagement.application.mapper.LineItemApplicationMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaRepository;
import com.syemon.invoicemanagement.infrastructure.InvoiceRepository;
import com.syemon.invoicemanagement.infrastructure.LineItemInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.LineItemJpaRepository;
import com.syemon.invoicemanagement.infrastructure.LineItemRepository;
import com.syemon.invoicemanagement.infrastructure.OwnerPostgresRepository;
import com.syemon.invoicemanagement.infrastructure.OwnerRepository;
import com.syemon.invoicemanagement.infrastructure.PostgresInvoiceRepository;
import com.syemon.invoicemanagement.infrastructure.PostgresLineItemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public CreateInvoiceApplicationService createInvoiceApplicationService(
            InvoiceRepository invoiceRepository,
            OwnerRepository ownerPostgresRepository,
            InvoiceMapper invoiceMapper
    ) {
        return new CreateInvoiceApplicationService(
                invoiceRepository,
                ownerPostgresRepository,
                invoiceMapper
        );
    }

    @Bean
    public LineItemApplicationMapper lineItemApplicationMapper() {
        return new LineItemApplicationMapper();
    }

    @Bean
    public InvoiceRepository postgresInvoiceRepository(
            InvoiceJpaRepository invoiceJpaRepository,
            InvoiceInfrastructureMapper invoiceInfrastructureMapper) {
        return new PostgresInvoiceRepository(invoiceJpaRepository, invoiceInfrastructureMapper);
    }

    @Bean
    public LineItemRepository postgresLineItemRepository(
            LineItemJpaRepository lineItemJpaRepository,
            LineItemInfrastructureMapper lineItemInfrastructureMapper
    ) {
        return new PostgresLineItemRepository(
                lineItemJpaRepository,
                lineItemInfrastructureMapper
        );
    }

    @Bean
    public InvoiceInfrastructureMapper invoiceInfrastructureMapper(LineItemInfrastructureMapper lineItemInfrastructureMapper) {
        return new InvoiceInfrastructureMapper(lineItemInfrastructureMapper);
    }

    @Bean
    public LineItemInfrastructureMapper lineItemInfrastructureMapper() {
        return new LineItemInfrastructureMapper();
    }
}
