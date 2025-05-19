package com.syemon.invoicemanagement.application.generate;

import com.syemon.invoicemanagement.application.mapper.InvoiceMapperImpl;
import com.syemon.invoicemanagement.application.mapper.LineItemMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateInvoiceServiceTest {

    private GenerateInvoiceService sut;

    @Mock
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    public void setUp() {
        sut = new GenerateInvoiceService(
                invoiceRepository,
                new InvoiceMapperImpl(Mappers.getMapper(LineItemMapper.class)));
    }

    @Test
    void calculateInvoiceValues_shouldDoNothing_whenNoInvoicesFound() {
        //given
        when(invoiceRepository.findInvoicesByStatusIn(
                GenerateInvoiceService.STATUSES_TO_GENERATE,
                GenerateInvoiceService.INVOICE_PER_QUERY_LIMIT
        )).thenReturn(Collections.emptyList());

        //when
        sut.calculateInvoiceValues();

        //then
        verify(invoiceRepository, never()).save(any());
    }
}