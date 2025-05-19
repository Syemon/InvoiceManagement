package com.syemon.invoicemanagement.application.mapper;

import com.syemon.invoicemanagement.application.AddressModel;
import com.syemon.invoicemanagement.application.CompanyModel;
import com.syemon.invoicemanagement.application.InvoiceModel;
import com.syemon.invoicemanagement.application.create.CreateInvoiceRequest;
import com.syemon.invoicemanagement.domain.Address;
import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.infrastructure.InvoiceJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LineItemMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "totalTaxAmount", ignore = true)
    @Mapping(target = "paymentLink", ignore = true)
    @Mapping(target = "paid", ignore = true)
    @Mapping(target = "sellerName", source = "seller.name")
    @Mapping(target = "sellerPhoneNumber", source = "seller.phoneNumber")
    @Mapping(target = "sellerEmail", source = "seller.email")
    @Mapping(target = "sellerAddressStreet", source = "seller.address.street")
    @Mapping(target = "sellerAddressCity", source = "seller.address.city")
    @Mapping(target = "sellerAddressCountry", source = "seller.address.country")
    @Mapping(target = "sellerAddressZipCode", source = "seller.address.postalCode")
    @Mapping(target = "buyerName", source = "buyer.name")
    @Mapping(target = "buyerPhoneNumber", source = "buyer.phoneNumber")
    @Mapping(target = "buyerEmail", source = "buyer.email")
    @Mapping(target = "buyerAddressStreet", source = "buyer.address.street")
    @Mapping(target = "buyerAddressCity", source = "buyer.address.city")
    @Mapping(target = "buyerAddressCountry", source = "buyer.address.country")
    @Mapping(target = "buyerAddressZipCode", source = "buyer.address.postalCode")
    @Mapping(target = "lineItems", source = "lineItems")
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "invoiceStatus", constant = "NEW")
    InvoiceJpaEntity toEntity(CreateInvoiceRequest request);

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "invoiceHeader", source = "invoiceHeader")
    @Mapping(target = "invoiceDate", source = "invoiceDate")
    @Mapping(target = "dueTime", source = "dueTime")
    @Mapping(target = "seller", expression = "java(mapSeller(invoiceJpaEntity))")
    @Mapping(target = "buyer", expression = "java(mapBuyer(invoiceJpaEntity))")
    @Mapping(target = "lineItems", source = "lineItems")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "paymentLink", source = "paymentLink")
    @Mapping(target = "paid", source = "paid")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "totalTaxAmount", source = "totalTaxAmount")
    Invoice toDomain(InvoiceJpaEntity invoiceJpaEntity);

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "invoiceHeader", source = "invoiceHeader")
    @Mapping(target = "invoiceDate", source = "invoiceDate")
    @Mapping(target = "dueTime", source = "dueTime")
    @Mapping(target = "seller", expression = "java(mapCompanySeller(invoiceJpaEntity))")
    @Mapping(target = "buyer", expression = "java(mapCompanyBuyer(invoiceJpaEntity))")
    @Mapping(target = "lineItems", source = "lineItems")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "totalTaxAmount", source = "totalTaxAmount")
    @Mapping(target = "paymentLink", source = "paymentLink")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "paid", source = "paid")
    @Mapping(target = "invoiceStatus", source = "invoiceStatus")
    InvoiceModel toModel(InvoiceJpaEntity invoiceJpaEntity);

    default CompanyModel mapCompanySeller(InvoiceJpaEntity entity) {
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

    default CompanyModel mapCompanyBuyer(InvoiceJpaEntity entity) {
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

    default Company mapSeller(InvoiceJpaEntity entity) {
        return new Company(
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
    }

    default Company mapBuyer(InvoiceJpaEntity entity) {
        return new Company(
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
    }
}