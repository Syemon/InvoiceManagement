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
import com.syemon.invoicemanagement.domain.Money;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaEntity;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {LineItemMapper.class})
public interface InvoiceApplicationMapper {

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "invoiceHeader", source = "invoiceHeader")
    @Mapping(target = "invoiceDate", source = "invoiceDate")
    @Mapping(target = "dueTime", source = "dueTime")
    @Mapping(target = "seller", expression = "java(mapSeller(invoiceJpaEntity))")
    @Mapping(target = "buyer", expression = "java(mapBuyer(invoiceJpaEntity))")
    @Mapping(target = "lineItems", source = "lineItems")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "totalTaxAmount", source = "totalTaxAmount")
    @Mapping(target = "paymentLink", source = "paymentLink")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "paid", source = "paid")
    @Mapping(target = "invoiceStatus", source = "invoiceStatus")
    InvoiceModel toModel(InvoiceJpaEntity invoiceJpaEntity);

    default CompanyModel mapSeller(InvoiceJpaEntity entity) {
        return new CompanyModel(
                entity.getSellerName(),
                entity.getSellerPhoneNumber(),
                entity.getSellerEmail(),
                new AddressModel(
                        entity.getSellerAddressStreet(),
                        entity.getSellerAddressCity(),
                        entity.getSellerAddressZipCode(),
                        entity.getSellerAddressCountry()
                )
        );
    }

    default CompanyModel mapBuyer(InvoiceJpaEntity entity) {
        return new CompanyModel(
                entity.getBuyerName(),
                entity.getBuyerPhoneNumber(),
                entity.getBuyerEmail(),
                new AddressModel(
                        entity.getBuyerAddressStreet(),
                        entity.getBuyerAddressCity(),
                        entity.getBuyerAddressZipCode(),
                        entity.getBuyerAddressCountry()
                )
        );
    }
}
