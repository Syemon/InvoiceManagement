package com.syemon.invoicemanagement.application.mapper;

import com.syemon.invoicemanagement.application.AddressModel;
import com.syemon.invoicemanagement.application.CompanyModel;
import com.syemon.invoicemanagement.application.InvoiceModel;
import com.syemon.invoicemanagement.application.create.CreateInvoiceRequest;
import com.syemon.invoicemanagement.application.create.LineItemModel;
import com.syemon.invoicemanagement.domain.Address;
import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.LineItem;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceApplicationMapper {

//    private final LineItemApplicationMapper lineItemMapper;

    public Invoice toDomain(CreateInvoiceRequest request) {
        Company seller = toDomain(request.seller());
        Company buyer = toDomain(request.buyer());

        List<LineItem> lineItems = request.lineItems().stream()
                .map(this::toLineItemDomain)
                .collect(Collectors.toList());

        return Invoice.builder()
                .invoiceHeader(request.invoiceHeader())
                .invoiceDate(request.invoiceDate())
                .dueTime(request.dueTime())
                .seller(seller)
                .buyer(buyer)
                .lineItems(lineItems)
                .currency(request.currency())
                .build();

    }

    public InvoiceModel toModel(Invoice invoice) {
        return InvoiceModel.builder()
                .uuid(invoice.getUuid())
                .invoiceHeader(invoice.getInvoiceHeader())
                .invoiceDate(invoice.getInvoiceDate())
                .dueTime(invoice.getDueTime())
                .seller(invoice.getSeller())
                .buyer(invoice.getBuyer())
                .lineItems(invoice.getLineItems())
                .totalAmount(invoice.getTotalAmount())
                .totalTaxAmount(invoice.getTotalTaxAmount())
                .paymentLink(invoice.getPaymentLink())
                .currency(invoice.getCurrency())
                .paid(invoice.isPaid())
                .build();
    }

    public LineItem toLineItemDomain(LineItemModel command) {
        return LineItem.builder()
                .description(command.description())
                .amountPerItem(command.amountPerItem())
                .quantity(command.quantity())
                .tax(command.tax())
                .build();
    }

    public Company toDomain(CompanyModel companyModel) {
        return new Company(
                companyModel.name(),
                companyModel.phoneNumber(),
                companyModel.email(),
                toDomain(companyModel.address())
        );
    }
    
    public Address toDomain(AddressModel addressModel) {
        return new Address(
                addressModel.street(),
                addressModel.city(),
                addressModel.postalCode(),
                addressModel.country()
        );
    }
}
