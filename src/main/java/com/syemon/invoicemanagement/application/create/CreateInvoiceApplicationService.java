package com.syemon.invoicemanagement.application.create;

import com.syemon.invoicemanagement.application.mapper.InvoiceApplicationMapper;
import com.syemon.invoicemanagement.domain.service.CreateInvoiceService;
import com.syemon.invoicemanagement.domain.Invoice;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class CreateInvoiceApplicationService {
    private final CreateInvoiceService createInvoiceService;
    private final InvoiceApplicationMapper invoiceApplicationMapper;

    @Transactional
    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest createInvoiceRequest) {
        Invoice invoice = invoiceApplicationMapper.toDomain(createInvoiceRequest);
        Invoice persistedInvoice = createInvoiceService.save(invoice);

        CreateInvoiceResponse response = new CreateInvoiceResponse();
        response.setStatus(200);
        response.setData(persistedInvoice);
        return response;
    }
}
