package com.syemon.invoicemanagement.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceTest {
    public static final String PRODUCT_1_DESCRIPTION = "Product 1";
    public static final String PRODUCT_2_DESCRIPTION = "Product 2";
    public static final String BUYER_STREET = "street 1/2";
    public static final String BUYER_CITY = "city1";
    public static final String BUYER_POSTAL_CODE = "80111";
    public static final String BUYER_COUNTRY = "Germany";
    public static final String BUYER_COMPANY_NAME = "name 1";
    public static final String BUYER_PHONE_NUMBER = "111222333";
    public static final String BUYER_EMAIL = "example1@example.com";
    public static final String SELLER_STREET = "street 2/3";
    public static final String SELLER_CITY = "city2";
    public static final String SELLER_POSTAL_CODE = "80222";
    public static final String SELLER_COUNTRY = "Italy";
    public static final String SELLER_COMPANY_NAME = "name 2";
    public static final String SELLER_PHONE_NUMBER = "111222444";
    public static final String SELLER_EMAIL = "example2@example.com";
    public static final DocumentType INVOICE_HEADER = DocumentType.INVOICE;
    public static final Currency EUR_CURRENCY = Currency.getInstance("EUR");

    @Test
    void calculateTotalAmountAndTotalTaxAmount() {
        //given
        List<LineItem> lineItems = List.of(
                new LineItem()
                        .setAmountPerItem(new BigDecimal("20.99"))
                        .setQuantity(10)
                        .setTax(new BigDecimal(21))
                        .setDescription(PRODUCT_1_DESCRIPTION),
                new LineItem()
                        .setAmountPerItem(new BigDecimal("1.99"))
                        .setQuantity(100)
                        .setTax(new BigDecimal(7))
                        .setDescription(PRODUCT_2_DESCRIPTION)
        );

        Address buyerAddress = new Address(
                BUYER_STREET,
                BUYER_CITY,
                BUYER_POSTAL_CODE,
                BUYER_COUNTRY
        );

        Company buyer = new Company(
                BUYER_COMPANY_NAME,
                BUYER_PHONE_NUMBER,
                BUYER_EMAIL,
                buyerAddress
        );

        Address sellerAddress = new Address(
                SELLER_STREET,
                SELLER_CITY,
                SELLER_POSTAL_CODE,
                SELLER_COUNTRY
        );

        Company seller = new Company(
                SELLER_COMPANY_NAME,
                SELLER_PHONE_NUMBER,
                SELLER_EMAIL,
                sellerAddress
        );

        OffsetDateTime invoiceDate = OffsetDateTime.now();
        OffsetDateTime dueDate = invoiceDate.plusMonths(1);
        Invoice invoice = new Invoice(
                lineItems,
                buyer,
                seller,
                dueDate,
                invoiceDate,
                INVOICE_HEADER,
                EUR_CURRENCY
        );

        Money expectedTotalAmount = Money.of(
                new BigDecimal("467.00"),
                EUR_CURRENCY
        );

        //when
        invoice.calculateTotalAmountAndTotalTaxAmount();

        //then
        assertThat(invoice.getTotalAmount()).isEqualTo(expectedTotalAmount);
        Money expectedTaxAmount = Money.of(
                new BigDecimal("58.10"),
                EUR_CURRENCY
        );
        assertThat(invoice.getTotalTaxAmount()).isEqualTo(expectedTaxAmount);

        assertThat(invoice.getUuid()).isNull();
        assertThat(invoice.getInvoiceHeader()).isEqualTo(INVOICE_HEADER);
        assertThat(invoice.getInvoiceDate()).isEqualTo(invoiceDate);
        assertThat(invoice.getDueTime()).isEqualTo(dueDate);
        assertThat(invoice.getSeller()).isNotNull();
        assertThat(invoice.getBuyer()).isNotNull();
        assertThat(invoice.getPaymentLink()).isNull();
        assertThat(invoice.isPaid()).isFalse();
        assertThat(invoice.getCurrency()).isEqualTo(EUR_CURRENCY);

        Company actualSeller = invoice.getSeller();
        assertThat(actualSeller.email()).isEqualTo(SELLER_EMAIL);
        assertThat(actualSeller.name()).isEqualTo(SELLER_COMPANY_NAME);
        assertThat(actualSeller.phoneNumber()).isEqualTo(SELLER_PHONE_NUMBER);
        assertThat(actualSeller.address()).isNotNull();

        Address actualSellerAddress = actualSeller.address();
        assertThat(actualSellerAddress.city()).isEqualTo(SELLER_CITY);
        assertThat(actualSellerAddress.country()).isEqualTo(SELLER_COUNTRY);
        assertThat(actualSellerAddress.postalCode()).isEqualTo(SELLER_POSTAL_CODE);
        assertThat(actualSellerAddress.street()).isEqualTo(SELLER_STREET);

        Company actualBuyer = invoice.getBuyer();
        assertThat(actualBuyer.email()).isEqualTo(BUYER_EMAIL);
        assertThat(actualBuyer.name()).isEqualTo(BUYER_COMPANY_NAME);
        assertThat(actualBuyer.phoneNumber()).isEqualTo(BUYER_PHONE_NUMBER);
        assertThat(actualBuyer.address()).isNotNull();

        Address actualBuyerAddress = actualBuyer.address();
        assertThat(actualBuyerAddress.city()).isEqualTo(BUYER_CITY);
        assertThat(actualBuyerAddress.country()).isEqualTo(BUYER_COUNTRY);
        assertThat(actualBuyerAddress.postalCode()).isEqualTo(BUYER_POSTAL_CODE);
        assertThat(actualBuyerAddress.street()).isEqualTo(BUYER_STREET);
    }

}