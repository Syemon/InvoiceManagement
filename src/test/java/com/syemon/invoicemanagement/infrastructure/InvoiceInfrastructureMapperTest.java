package com.syemon.invoicemanagement.infrastructure;

import com.syemon.invoicemanagement.domain.Address;
import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.DocumentType;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceInfrastructureMapperTest {

    private static final UUID STATIC_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final OffsetDateTime STATIC_INVOICE_DATE = OffsetDateTime.parse("2023-01-01T10:00:00Z");
    private static final OffsetDateTime STATIC_DUE_TIME = OffsetDateTime.parse("2023-02-01T10:00:00Z");

    private static final String SELLER_NAME = "Seller Name";
    private static final String SELLER_PHONE = "123456789";
    private static final String SELLER_EMAIL = "seller@example.com";
    private static final String SELLER_ADDRESS_STREET = "Street 1";
    private static final String SELLER_ADDRESS_CITY = "City";
    private static final String SELLER_ADDRESS_ZIP_CODE = "12345";
    private static final String SELLER_ADDRESS_COUNTRY = "Country";

    private static final String BUYER_NAME = "Buyer Name";
    private static final String BUYER_PHONE = "987654321";
    private static final String BUYER_EMAIL = "buyer@example.com";
    private static final String BUYER_ADDRESS_STREET = "Street 2";
    private static final String BUYER_ADDRESS_CITY = "City";
    private static final String BUYER_ADDRESS_ZIP_CODE = "54321";
    private static final String BUYER_ADDRESS_COUNTRY = "Country";

    private static final String ITEM_DESCRIPTION = "Item 1";
    private static final BigDecimal ITEM_AMOUNT_PER_ITEM = BigDecimal.TEN;
    private static final BigDecimal ITEM_TOTAL_AMOUNT = BigDecimal.valueOf(100);
    private static final BigDecimal ITEM_TOTAL_TAX_AMOUNT = BigDecimal.valueOf(10);
    private static final int ITEM_QUANTITY = 2;
    private static final BigDecimal ITEM_TAX = BigDecimal.valueOf(5);

    private static final BigDecimal TOTAL_AMOUNT = BigDecimal.valueOf(200);
    private static final BigDecimal TOTAL_TAX_AMOUNT = BigDecimal.valueOf(20);
    private static final String PAYMENT_LINK = "http://payment.link";
    private static final Currency CURRENCY = Currency.getInstance("USD");

    private final InvoiceInfrastructureMapper sut = new InvoiceInfrastructureMapper(
            new LineItemInfrastructureMapper()
    );

    @Test
    void toEntity() {
        Address sellerAddress = new Address(
                SELLER_ADDRESS_STREET,
                SELLER_ADDRESS_CITY,
                SELLER_ADDRESS_ZIP_CODE,
                SELLER_ADDRESS_COUNTRY
        );
        Company seller = new Company(
                SELLER_NAME,
                SELLER_PHONE,
                SELLER_EMAIL,
                sellerAddress
        );

        Address buyerAddress = new Address(
                BUYER_ADDRESS_STREET,
                BUYER_ADDRESS_CITY,
                BUYER_ADDRESS_ZIP_CODE,
                BUYER_ADDRESS_COUNTRY
        );
        Company buyer = new Company(
                BUYER_NAME,
                BUYER_PHONE,
                BUYER_EMAIL,
                buyerAddress
        );

        LineItem lineItem = LineItem.builder()
                .description(ITEM_DESCRIPTION)
                .amountPerItem(ITEM_AMOUNT_PER_ITEM)
                .totalAmount(ITEM_TOTAL_AMOUNT)
                .totalTaxAmount(ITEM_TOTAL_TAX_AMOUNT)
                .quantity(ITEM_QUANTITY)
                .tax(ITEM_TAX)
                .build();
        List<LineItem> lineItems = List.of(lineItem);

        Money totalAmount = Money.of(TOTAL_AMOUNT, CURRENCY);
        Money totalTaxAmount = Money.of(TOTAL_TAX_AMOUNT, CURRENCY);

        Invoice invoice = Invoice.builder()
                .uuid(STATIC_UUID)
                .invoiceHeader(DocumentType.INVOICE)
                .invoiceDate(STATIC_INVOICE_DATE)
                .dueTime(STATIC_DUE_TIME)
                .seller(seller)
                .buyer(buyer)
                .lineItems(lineItems)
                .totalAmount(totalAmount)
                .totalTaxAmount(totalTaxAmount)
                .paymentLink(PAYMENT_LINK)
                .currency(CURRENCY)
                .paid(true)
                .build();

        //when
        InvoiceJpaEntity entity = sut.toEntity(invoice);

        //then
        assertThat(entity).isNotNull();
        assertThat(entity.getUuid()).isEqualTo(STATIC_UUID);
        assertThat(entity.getInvoiceHeader()).isEqualTo(DocumentType.INVOICE);
        assertThat(entity.getInvoiceDate()).isEqualTo(STATIC_INVOICE_DATE);
        assertThat(entity.getDueTime()).isEqualTo(STATIC_DUE_TIME);
        assertThat(entity.getSellerName()).isEqualTo(SELLER_NAME);
        assertThat(entity.getSellerPhoneNumber()).isEqualTo(SELLER_PHONE);
        assertThat(entity.getSellerEmail()).isEqualTo(SELLER_EMAIL);
        assertThat(entity.getSellerAddressStreet()).isEqualTo(SELLER_ADDRESS_STREET);
        assertThat(entity.getSellerAddressCity()).isEqualTo(SELLER_ADDRESS_CITY);
        assertThat(entity.getSellerAddressZipCode()).isEqualTo(SELLER_ADDRESS_ZIP_CODE);
        assertThat(entity.getSellerAddressCountry()).isEqualTo(SELLER_ADDRESS_COUNTRY);
        assertThat(entity.getBuyerName()).isEqualTo(BUYER_NAME);
        assertThat(entity.getBuyerPhoneNumber()).isEqualTo(BUYER_PHONE);
        assertThat(entity.getBuyerEmail()).isEqualTo(BUYER_EMAIL);
        assertThat(entity.getBuyerAddressStreet()).isEqualTo(BUYER_ADDRESS_STREET);
        assertThat(entity.getBuyerAddressCity()).isEqualTo(BUYER_ADDRESS_CITY);
        assertThat(entity.getBuyerAddressZipCode()).isEqualTo(BUYER_ADDRESS_ZIP_CODE);
        assertThat(entity.getBuyerAddressCountry()).isEqualTo(BUYER_ADDRESS_COUNTRY);
        assertThat(entity.getLineItems()).hasSize(1);
        assertThat(entity.getTotalAmount().intValue()).isEqualTo(TOTAL_AMOUNT.intValue());
        assertThat(entity.getTotalTaxAmount().intValue()).isEqualTo(TOTAL_TAX_AMOUNT.intValue());
        assertThat(entity.getPaymentLink()).isEqualTo(PAYMENT_LINK);
        assertThat(entity.getCurrency()).isEqualTo(CURRENCY);
        assertThat(entity.isPaid()).isTrue();
    }

    @Test
    void toDomain() {
        InvoiceJpaEntity entity = new InvoiceJpaEntity()
            .setUuid(STATIC_UUID)
            .setInvoiceHeader(DocumentType.INVOICE)
            .setInvoiceDate(STATIC_INVOICE_DATE)
            .setDueTime(STATIC_DUE_TIME)
            .setSellerName(SELLER_NAME)
            .setSellerPhoneNumber(SELLER_PHONE)
            .setSellerEmail(SELLER_EMAIL)
            .setSellerAddressStreet(SELLER_ADDRESS_STREET)
            .setSellerAddressCity(SELLER_ADDRESS_CITY)
            .setSellerAddressZipCode(SELLER_ADDRESS_ZIP_CODE)
            .setSellerAddressCountry(SELLER_ADDRESS_COUNTRY)
            .setBuyerName(BUYER_NAME)
            .setBuyerPhoneNumber(BUYER_PHONE)
            .setBuyerEmail(BUYER_EMAIL)
            .setBuyerAddressStreet(BUYER_ADDRESS_STREET)
            .setBuyerAddressCity(BUYER_ADDRESS_CITY)
            .setBuyerAddressZipCode(BUYER_ADDRESS_ZIP_CODE)
            .setBuyerAddressCountry(BUYER_ADDRESS_COUNTRY)
            .setLineItems(List.of(new LineItemJpaEntity()))
            .setTotalAmount(TOTAL_AMOUNT)
            .setTotalTaxAmount(TOTAL_TAX_AMOUNT)
            .setPaymentLink(PAYMENT_LINK)
            .setCurrency(CURRENCY)
            .setPaid(true);

        //when
        Invoice invoice = sut.toDomain(entity);

        //then
        assertThat(invoice).isNotNull();
        assertThat(invoice.getUuid()).isEqualTo(STATIC_UUID);
        assertThat(invoice.getInvoiceHeader()).isEqualTo(DocumentType.INVOICE);
        assertThat(invoice.getInvoiceDate()).isEqualTo(STATIC_INVOICE_DATE);
        assertThat(invoice.getDueTime()).isEqualTo(STATIC_DUE_TIME);
        assertThat(invoice.getSeller().name()).isEqualTo(SELLER_NAME);
        assertThat(invoice.getSeller().phoneNumber()).isEqualTo(SELLER_PHONE);
        assertThat(invoice.getSeller().email()).isEqualTo(SELLER_EMAIL);
        assertThat(invoice.getSeller().address().street()).isEqualTo(SELLER_ADDRESS_STREET);
        assertThat(invoice.getSeller().address().city()).isEqualTo(SELLER_ADDRESS_CITY);
        assertThat(invoice.getSeller().address().postalCode()).isEqualTo(SELLER_ADDRESS_ZIP_CODE);
        assertThat(invoice.getSeller().address().country()).isEqualTo(SELLER_ADDRESS_COUNTRY);
        assertThat(invoice.getBuyer().name()).isEqualTo(BUYER_NAME);
        assertThat(invoice.getBuyer().phoneNumber()).isEqualTo(BUYER_PHONE);
        assertThat(invoice.getBuyer().email()).isEqualTo(BUYER_EMAIL);
        assertThat(invoice.getBuyer().address().street()).isEqualTo(BUYER_ADDRESS_STREET);
        assertThat(invoice.getBuyer().address().city()).isEqualTo(BUYER_ADDRESS_CITY);
        assertThat(invoice.getBuyer().address().postalCode()).isEqualTo(BUYER_ADDRESS_ZIP_CODE);
        assertThat(invoice.getBuyer().address().country()).isEqualTo(BUYER_ADDRESS_COUNTRY);
        assertThat(invoice.getLineItems()).hasSize(1);
        assertThat(invoice.getTotalAmount().getAmount().intValue()).isEqualTo(TOTAL_AMOUNT.intValue());
        assertThat(invoice.getTotalTaxAmount().getAmount().intValue()).isEqualTo(TOTAL_TAX_AMOUNT.intValue());
        assertThat(invoice.getPaymentLink()).isEqualTo(PAYMENT_LINK);
        assertThat(invoice.getCurrency()).isEqualTo(CURRENCY);
        assertThat(invoice.isPaid()).isTrue();
    }
}