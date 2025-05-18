package com.syemon.invoicemanagement.domain;

import com.syemon.invoicemanagement.application.AddressModel;
import com.syemon.invoicemanagement.application.CompanyModel;
import com.syemon.invoicemanagement.application.InvoiceModel;
import com.syemon.invoicemanagement.application.create.CreateInvoiceApplicationService;
import com.syemon.invoicemanagement.application.create.CreateInvoiceRequest;
import com.syemon.invoicemanagement.application.create.CreateInvoiceResponse;
import com.syemon.invoicemanagement.application.create.LineItemModel;
import com.syemon.invoicemanagement.application.mapper.InvoiceApplicationMapper;
import com.syemon.invoicemanagement.application.mapper.LineItemApplicationMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.InvoiceRepository;
import com.syemon.invoicemanagement.infrastructure.LineItemInfrastructureMapper;
import com.syemon.invoicemanagement.infrastructure.OwnerJpaEntity;
import com.syemon.invoicemanagement.infrastructure.OwnerPostgresRepository;
import com.syemon.invoicemanagement.infrastructure.PostgresInvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateInvoiceApplicationServiceTest {

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
    public static final String OWNER_USERNAME = "name";

    //    @InjectMocks
    private CreateInvoiceApplicationService sut;

    @Mock
    private PostgresInvoiceRepository invoiceRepository;
    @Spy
    private InvoiceApplicationMapper invoiceApplicationMapper;
    @Mock
    private OwnerPostgresRepository ownerPostgresRepository;
    private InvoiceInfrastructureMapper invoiceInfrastructureMapper;

    @BeforeEach
    public void setUp() {
        invoiceInfrastructureMapper = new InvoiceInfrastructureMapper(
                new LineItemInfrastructureMapper()
        );
        sut = new CreateInvoiceApplicationService(
                invoiceRepository,
                invoiceApplicationMapper,
                ownerPostgresRepository,
                invoiceInfrastructureMapper
        );
    }

    @Test
    void createInvoice() {
        //given
        OffsetDateTime invoiceDate = OffsetDateTime.now();
        OffsetDateTime dueTime = invoiceDate.plusMonths(1);

        AddressModel buyerAddress = new AddressModel(
                BUYER_STREET,
                BUYER_CITY,
                BUYER_POSTAL_CODE,
                BUYER_COUNTRY
        );

        CompanyModel buyer = new CompanyModel(
                BUYER_COMPANY_NAME,
                BUYER_PHONE_NUMBER,
                BUYER_EMAIL,
                buyerAddress
        );

        AddressModel sellerAddress = new AddressModel(
                SELLER_STREET,
                SELLER_CITY,
                SELLER_POSTAL_CODE,
                SELLER_COUNTRY
        );

        CompanyModel seller = new CompanyModel(
                SELLER_COMPANY_NAME,
                SELLER_PHONE_NUMBER,
                SELLER_EMAIL,
                sellerAddress
        );

        List<LineItemModel> lineItems = List.of(
                new LineItemModel(
                        PRODUCT_DESCRIPTION,
                        AMOUNT_PER_ITEM,
                        QUANTITY,
                        TAX));

        CreateInvoiceRequest request = new CreateInvoiceRequest(
                INVOICE_HEADER,
                invoiceDate,
                dueTime,
                seller,
                buyer,
                lineItems,
                EUR_CURRENCY
        );
        Owner owner = new Owner(OWNER_USERNAME, "encryptedPassword");

        when(invoiceRepository.save(Mockito.any())).thenAnswer(invocation -> invoiceInfrastructureMapper.toDomain(invocation.getArgument(0)));
        OwnerJpaEntity ownerJpaEntity = new OwnerJpaEntity().setUsername(OWNER_USERNAME);
        when(ownerPostgresRepository.findByUsername(owner.getUsername())).thenReturn(Optional.of(ownerJpaEntity));

        //when
        CreateInvoiceResponse response = sut.createInvoice(request, owner);

        //then
        assertThat(response.getData()).isNotNull();
        InvoiceModel actual = response.getData();
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