package com.syemon.invoicemanagement.application;

import com.syemon.invoicemanagement.application.create.CreateInvoiceApplicationService;
import com.syemon.invoicemanagement.application.create.CreateInvoiceRequest;
import com.syemon.invoicemanagement.application.create.CreateInvoiceResponse;
import com.syemon.invoicemanagement.domain.Owner;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<CreateInvoiceResponse> create(
            @RequestBody @Validated CreateInvoiceRequest createInvoiceRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        CreateInvoiceResponse response = createInvoiceService.createInvoice(
                createInvoiceRequest,
                new Owner(userDetails.getUsername(), userDetails.getPassword())
        );
        return ResponseEntity.ok(response);
    }
}
