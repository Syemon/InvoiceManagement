package com.syemon.invoicemanagement.configuration;

import com.syemon.invoicemanagement.application.create.CreateInvoiceApplicationService;
import com.syemon.invoicemanagement.application.mapper.InvoiceApplicationMapper;
import com.syemon.invoicemanagement.application.mapper.LineItemApplicationMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaRepository;
import com.syemon.invoicemanagement.infrastructure.InvoiceRepository;
import com.syemon.invoicemanagement.infrastructure.LineItemInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.LineItemJpaRepository;
import com.syemon.invoicemanagement.infrastructure.OwnerPostgresRepository;
import com.syemon.invoicemanagement.infrastructure.PostgresInvoiceRepository;
import com.syemon.invoicemanagement.infrastructure.PostgresLineItemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public CreateInvoiceApplicationService createInvoiceApplicationService(
            PostgresInvoiceRepository invoiceRepository,
            InvoiceApplicationMapper invoiceApplicationMapper,
            OwnerPostgresRepository ownerPostgresRepository,
            InvoiceInfrastructureMapper invoiceInfrastructureMapper
    ) {
        return new CreateInvoiceApplicationService(
                invoiceRepository,
                invoiceApplicationMapper,
                ownerPostgresRepository,
                invoiceInfrastructureMapper
        );
    }

    @Bean
    public InvoiceApplicationMapper invoiceApplicationMapper() {
        return new InvoiceApplicationMapper();
    }

    @Bean
    public LineItemApplicationMapper lineItemApplicationMapper() {
        return new LineItemApplicationMapper();
    }

    @Bean
    public PostgresInvoiceRepository postgresInvoiceRepository(
            InvoiceJpaRepository invoiceJpaRepository,
            InvoiceInfrastructureMapper invoiceInfrastructureMapper) {
        return new PostgresInvoiceRepository(invoiceJpaRepository, invoiceInfrastructureMapper);
    }

    @Bean
    public PostgresLineItemRepository postgresLineItemRepository(
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
