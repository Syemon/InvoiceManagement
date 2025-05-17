package com.syemon.invoicemanagement.application;

import com.syemon.invoicemanagement.domain.CreateInvoiceService;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceCommand;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class CreateInvoiceApplicationService {
    private final CreateInvoiceService createInvoiceService;
    private final InvoiceApplicationMapper invoiceApplicationMapper;

    @Transactional
    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest createInvoiceRequest) {
        InvoiceCommand invoiceCommand = invoiceApplicationMapper.toCommand(createInvoiceRequest);
        Invoice invoice = createInvoiceService.createInvoice(invoiceCommand);

        CreateInvoiceResponse response = new CreateInvoiceResponse();
        response.setStatus(200);
        response.setData(invoice);
        return response;
    }
}
