package com.syemon.invoicemanagement.application;

import com.syemon.invoicemanagement.domain.Invoice;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class InvoiceRestController {

    private final CreateInvoiceApplicationService createInvoiceService;

    @PostMapping("invoice")
    public ResponseEntity<Invoice> create(@RequestBody @Validated CreateInvoiceRequest createInvoiceRequest) {
        Invoice invoice = createInvoiceService.createInvoice(createInvoiceRequest);
        return ResponseEntity.ok(invoice);
    }
}
