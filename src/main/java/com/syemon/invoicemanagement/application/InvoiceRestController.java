package com.syemon.invoicemanagement.application;

import jakarta.validation.Valid;
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
    //TODO: Create separete requests objects for each request model? Maybe get rid of command and map straight to domain object (now 3 mappings along the way)?
    @PostMapping("invoice")
    public ResponseEntity<CreateInvoiceResponse> create(@RequestBody @Validated CreateInvoiceRequest createInvoiceRequest) {
        CreateInvoiceResponse response = createInvoiceService.createInvoice(createInvoiceRequest);
        return ResponseEntity.ok(response);
    }
}
