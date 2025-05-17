package com.syemon.invoicemanagement.configuration;

import com.syemon.invoicemanagement.application.CreateInvoiceApplicationService;
import com.syemon.invoicemanagement.application.InvoiceApplicationMapper;
import com.syemon.invoicemanagement.application.LineItemApplicationMapper;
import com.syemon.invoicemanagement.domain.CreateInvoiceService;
import com.syemon.invoicemanagement.domain.InvoiceMapper;
import com.syemon.invoicemanagement.domain.LineItemMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaRepository;
import com.syemon.invoicemanagement.infrastructure.LineItemInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.PostgresInvoiceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public CreateInvoiceApplicationService createInvoiceApplicationService(
            CreateInvoiceService createInvoiceService,
            InvoiceApplicationMapper invoiceApplicationMapper
    ) {
        return new CreateInvoiceApplicationService(createInvoiceService, invoiceApplicationMapper);
    }

    @Bean
    public InvoiceApplicationMapper invoiceApplicationMapper(LineItemApplicationMapper lineItemApplicationMapper) {
        return new InvoiceApplicationMapper(lineItemApplicationMapper);
    }

    @Bean
    public LineItemApplicationMapper lineItemApplicationMapper() {
        return new LineItemApplicationMapper();
    }

    @Bean
    public InvoiceMapper invoiceMapper(LineItemMapper lineItemMapper) {
        return new InvoiceMapper(lineItemMapper);
    }

    @Bean
    public LineItemMapper lineItemMapper() {
        return new LineItemMapper();
    }

    @Bean
    public CreateInvoiceService createInvoiceService(
            PostgresInvoiceRepository postgresInvoiceRepository,
            InvoiceMapper invoiceMapper
    ) {
        return new CreateInvoiceService(postgresInvoiceRepository, invoiceMapper);
    }

    @Bean
    public PostgresInvoiceRepository postgresInvoiceRepository(
            InvoiceJpaRepository invoiceJpaRepository,
            InvoiceInfrastructureMapper invoiceInfrastructureMapper) {
        return new PostgresInvoiceRepository(invoiceJpaRepository, invoiceInfrastructureMapper);
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
