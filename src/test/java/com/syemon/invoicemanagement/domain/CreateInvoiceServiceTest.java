package com.syemon.invoicemanagement.domain;

import com.syemon.invoicemanagement.domain.service.CreateInvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateInvoiceServiceTest {

    private static final String PRODUCT_DESCRIPTION = "Product 1";
    private static final String BUYER_STREET = "street 1/2";
    private static final String BUYER_CITY = "city1";
    private static final String BUYER_POSTAL_CODE = "80111";
    private static final String BUYER_COUNTRY = "Germany";
    private static final String BUYER_COMPANY_NAME = "name 1";
    private static final String BUYER_PHONE_NUMBER = "111222333";
    private static final String BUYER_EMAIL = "example1@example.com";
    private static final String SELLER_STREET = "street 2/3";
    private static final String SELLER_CITY = "city2";
    private static final String SELLER_POSTAL_CODE = "80222";
    private static final String SELLER_COUNTRY = "Italy";
    private static final String SELLER_COMPANY_NAME = "name 2";
    private static final String SELLER_PHONE_NUMBER = "111222444";
    private static final String SELLER_EMAIL = "example2@example.com";

    private static final DocumentType INVOICE_HEADER = DocumentType.INVOICE;
    private static final Currency EUR_CURRENCY = Currency.getInstance("EUR");
    private static final BigDecimal AMOUNT_PER_ITEM = new BigDecimal("33.99");
    private static final int QUANTITY = 14;
    private static final BigDecimal TAX = new BigDecimal("17");

    @InjectMocks
    private CreateInvoiceService sut;

    @Mock
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    public void setUp() {
        sut = new CreateInvoiceService(
                invoiceRepository
        );
    }

    @Test
    void save() {
        //given
        OffsetDateTime invoiceDate = OffsetDateTime.now();
        OffsetDateTime dueTime = invoiceDate.plusMonths(1);

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

        List<LineItem> lineItems = List.of(
                LineItem.builder()
                        .description(PRODUCT_DESCRIPTION)
                        .amountPerItem(AMOUNT_PER_ITEM)
                        .quantity(QUANTITY)
                        .tax(TAX)
                        .build()
        );

        Invoice invoice = Invoice.builder()
                .invoiceHeader(INVOICE_HEADER)
                .invoiceDate(invoiceDate)
                .dueTime(dueTime)
                .seller(seller)
                .buyer(buyer)
                .lineItems(lineItems)
                .currency(EUR_CURRENCY)
                .build();

        when(invoiceRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Invoice actual = sut.save(invoice);

        //then
        assertThat(actual.getUuid()).isNotNull();
        assertThat(actual.getInvoiceHeader()).isEqualTo(INVOICE_HEADER);
        assertThat(actual.getInvoiceDate()).isEqualTo(invoiceDate);
        assertThat(actual.getDueTime()).isEqualTo(dueTime);
        assertThat(actual.getSeller()).isNotNull();
        assertThat(actual.getBuyer()).isNotNull();
        assertThat(actual.getPaymentLink()).isNull();
        assertThat(actual.isPaid()).isFalse();
        assertThat(actual.getCurrency()).isEqualTo(EUR_CURRENCY);

        Company actualSeller = actual.getSeller();
        assertThat(actualSeller.email()).isEqualTo(SELLER_EMAIL);
        assertThat(actualSeller.name()).isEqualTo(SELLER_COMPANY_NAME);
        assertThat(actualSeller.phoneNumber()).isEqualTo(SELLER_PHONE_NUMBER);
        assertThat(actualSeller.address()).isNotNull();

        Address actualSellerAddress = actualSeller.address();
        assertThat(actualSellerAddress.city()).isEqualTo(SELLER_CITY);
        assertThat(actualSellerAddress.country()).isEqualTo(SELLER_COUNTRY);
        assertThat(actualSellerAddress.postalCode()).isEqualTo(SELLER_POSTAL_CODE);
        assertThat(actualSellerAddress.street()).isEqualTo(SELLER_STREET);

        Company actualBuyer = actual.getBuyer();
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