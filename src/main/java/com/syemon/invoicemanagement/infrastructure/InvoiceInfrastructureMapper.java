package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Address;
import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.domain.Money;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceInfrastructureMapper {

    private final LineItemInfrastructureMapper lineItemInfrastructureMapper;

    public InvoiceJpaEntity toEntity(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        InvoiceJpaEntity invoiceJpaEntity = new InvoiceJpaEntity()
                .setUuid(invoice.getUuid())
                .setInvoiceHeader(invoice.getInvoiceHeader())
                .setInvoiceDate(invoice.getInvoiceDate())
                .setDueTime(invoice.getDueTime())
                .setSellerName(invoice.getSeller().name())
                .setSellerPhoneNumber(invoice.getSeller().phoneNumber())
                .setSellerEmail(invoice.getSeller().email())
                .setSellerAddressStreet(invoice.getSeller().address().street())
                .setSellerAddressCity(invoice.getSeller().address().city())
                .setSellerAddressZipCode(invoice.getSeller().address().postalCode())
                .setSellerAddressCountry(invoice.getSeller().address().country())
                .setBuyerName(invoice.getBuyer().name())
                .setBuyerPhoneNumber(invoice.getBuyer().phoneNumber())
                .setBuyerEmail(invoice.getBuyer().email())
                .setBuyerAddressStreet(invoice.getBuyer().address().street())
                .setBuyerAddressCity(invoice.getBuyer().address().city())
                .setBuyerAddressZipCode(invoice.getBuyer().address().postalCode())
                .setBuyerAddressCountry(invoice.getBuyer().address().country())

                .setTotalAmount(Optional.ofNullable(invoice.getTotalAmount()).map(Money::getAmount).orElse(null))
                .setTotalTaxAmount(Optional.ofNullable(invoice.getTotalTaxAmount()).map(Money::getAmount).orElse(null))
                .setPaymentLink(invoice.getPaymentLink())
                .setCurrency(invoice.getCurrency())
                .setPaid(invoice.isPaid());

                invoiceJpaEntity.setLineItems(invoice.getLineItems().stream()
                    .map(lineItemInfrastructureMapper::toEntity).map(lineItemJpaEntity -> lineItemJpaEntity.setInvoice(invoiceJpaEntity))
                    .toList());

                return invoiceJpaEntity;
    }

    public Invoice toDomain(InvoiceJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Company seller = new Company(
                entity.getSellerName(),
                entity.getSellerPhoneNumber(),
                entity.getSellerEmail(),
                new Address(
                        entity.getSellerAddressStreet(),
                        entity.getSellerAddressCity(),
                        entity.getSellerAddressZipCode(),
                        entity.getSellerAddressCountry()
                )
        );
        Company buyer = new Company(
                entity.getBuyerName(),
                entity.getBuyerPhoneNumber(),
                entity.getBuyerEmail(),
                new Address(
                        entity.getBuyerAddressStreet(),
                        entity.getBuyerAddressCity(),
                        entity.getBuyerAddressZipCode(),
                        entity.getBuyerAddressCountry()
                )
        );
        List<LineItem> lineItems = entity.getLineItems()
                .stream()
                .map(lineItemInfrastructureMapper::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));

        return Invoice.builder()
                .uuid(entity.getUuid())
                .invoiceHeader(entity.getInvoiceHeader())
                .invoiceDate(entity.getInvoiceDate())
                .dueTime(entity.getDueTime())
                .seller(seller)
                .buyer(buyer)
                .lineItems(lineItems)
                .totalAmount(
                        entity.getTotalAmount() != null
                                ? Money.of(entity.getTotalAmount(), entity.getCurrency())
                                : null
                )
                .totalTaxAmount(
                        entity.getTotalTaxAmount() != null
                                ? Money.of(entity.getTotalTaxAmount(), entity.getCurrency())
                                : null
                )
                .paymentLink(entity.getPaymentLink())
                .currency(entity.getCurrency())
                .paid(entity.isPaid())
                .build();
    }
}