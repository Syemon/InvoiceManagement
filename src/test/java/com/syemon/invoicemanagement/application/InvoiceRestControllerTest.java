package com.syemon.invoicemanagement.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syemon.invoicemanagement.TestcontainersConfiguration;
import com.syemon.invoicemanagement.application.security.UserApplicationService;
import com.syemon.invoicemanagement.application.create.CreateInvoiceRequest;
import com.syemon.invoicemanagement.application.create.CreateInvoiceResponse;
import com.syemon.invoicemanagement.application.create.LineItemModel;
import com.syemon.invoicemanagement.domain.Address;
import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.DocumentType;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.repository.InvoiceRepository;
import com.syemon.invoicemanagement.domain.LineItem;
import com.syemon.invoicemanagement.infrastructure.OwnerJpaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)

class InvoiceRestControllerTest {

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

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserApplicationService userApplicationService;

    @Test
    void create_shouldReturn400_whenNotValid() throws Exception {
        //given
        String username = "username";
        String password = "username";
        OwnerJpaEntity ownerJpaEntity = new OwnerJpaEntity()
                .setUsername(username)
                .setPassword(password);
        userApplicationService.createUser(ownerJpaEntity);

        String body = objectMapper.writeValueAsString(
                new CreateInvoiceRequest(null, null, null, null, null, null, null)
        );

        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("buyer", "must not be null");
        expectedErrors.put("currency", "must not be null");
        expectedErrors.put("dueTime", "must not be null");
        expectedErrors.put("invoiceDate", "must not be null");
        expectedErrors.put("invoiceHeader", "must not be null");
        expectedErrors.put("lineItems", "must not be null");
        expectedErrors.put("seller", "must not be null");

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf())
                        .with(httpBasic(username, password))
        );

        //then
        String rawBody = resultActions
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        GenericRestResponse baseResponse = objectMapper.readValue(rawBody, GenericRestResponse.class);
        assertThat(baseResponse.getInvalidFields()).hasSize(7);
        assertThat(baseResponse.getInvalidFields()).isEqualTo(expectedErrors);
        assertThat(baseResponse.getStatus()).isEqualTo(400);
    }

    @Test
    void create_shouldReturn400_whenNotValidNestedObjects() throws Exception {
        //given
        CompanyModel emptyCompany = new CompanyModel(null, null, null, null);

        String body = objectMapper.writeValueAsString(
                new CreateInvoiceRequest(
                        DocumentType.INVOICE,
                        OffsetDateTime.now(),
                        OffsetDateTime.now().plusMonths(1),
                        emptyCompany,
                        emptyCompany,
                        new ArrayList<>(),
                        Currency.getInstance("EUR")
        ));

        Map<String, String> expectedErrors = Map.of(
                "lineItems", "size must be between 1 and 1000",
                "seller.address", "must not be null",
                "seller.name", "must not be blank",
                "seller.phoneNumber", "must not be blank",
                "buyer.address", "must not be null",
                "buyer.phoneNumber", "must not be blank",
                "buyer.name", "must not be blank"
        );

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf())
                        .with(httpBasic("admin", "admin123"))
        );

        //then
        String rawBody = resultActions
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        GenericRestResponse baseResponse = objectMapper.readValue(rawBody, GenericRestResponse.class);
        assertThat(baseResponse.getInvalidFields()).hasSize(7);
        assertThat(baseResponse.getInvalidFields()).isEqualTo(expectedErrors);
        assertThat(baseResponse.getStatus()).isEqualTo(400);
    }

    @ParameterizedTest
    @MethodSource("create_shouldReturn400_whenNotValidLineItemsProvider")
    void create_shouldReturn400_whenNotValidLineItems(
            String description,
            BigDecimal amountPerItem,
            Integer quantity,
            BigDecimal tax,
            Map<String, String> expectedErrors
    ) throws Exception {
        //given
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

        String body = objectMapper.writeValueAsString(
                new CreateInvoiceRequest(
                        DocumentType.INVOICE,
                        OffsetDateTime.now(),
                        OffsetDateTime.now().plusMonths(1),
                        buyer,
                        buyer,
                        List.of(
                            new LineItemModel(
                                    description,
                                    amountPerItem,
                                    quantity,
                                    tax
                            )
                        ),
                        Currency.getInstance("EUR")
                ));

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf())
                        .with(httpBasic("admin", "admin123"))

        );

        //then
        String rawBody = resultActions
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        GenericRestResponse baseResponse = objectMapper.readValue(rawBody, GenericRestResponse.class);
        assertThat(baseResponse.getInvalidFields()).isEqualTo(expectedErrors);
        assertThat(baseResponse.getStatus()).isEqualTo(400);
    }

    public static Stream<Arguments> create_shouldReturn400_whenNotValidLineItemsProvider() {
        return Stream.of(
                Arguments.of(
                        null,
                        null,
                        null,
                        null,
                        Map.of(
                                "lineItems[0].amountPerItem", "must not be null",
                                "lineItems[0].description", "must not be blank",
                                "lineItems[0].quantity", "must not be null",
                                "lineItems[0].tax", "must not be null")
                ),
                Arguments.of(
                        "a".repeat(1001),
                        BigDecimal.ZERO,
                        0,
                        BigDecimal.ZERO,
                        Map.of(
                                "lineItems[0].quantity", "must be between 1 and 9999",
                                "lineItems[0].amountPerItem", "must be between 1 and 9999999",
                                "lineItems[0].description", "length must be between 0 and 999"
                        )
                ),
                Arguments.of(
                        "a",
                        new BigDecimal(99999991),
                        10000,
                        new BigDecimal(1001),
                        Map.of(
                                "lineItems[0].amountPerItem", "must be between 1 and 9999999",
                                "lineItems[0].quantity", "must be between 1 and 9999",
                                "lineItems[0].tax", "must be between 0 and 1000"
                        )
                )
        );
    }

    @Test
    void create_shouldPersistInvoice() throws Exception {
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

        List<LineItemModel> lineItemsCommand = List.of(
                new LineItemModel(
                        PRODUCT_DESCRIPTION,
                        AMOUNT_PER_ITEM,
                        QUANTITY,
                        TAX
                )
        );

        CreateInvoiceRequest request = new CreateInvoiceRequest(
                INVOICE_HEADER,
                invoiceDate,
                dueTime,
                seller,
                buyer,
                lineItemsCommand,
                EUR_CURRENCY
        );





        String username = "username3";
        String password = "username3";
        OwnerJpaEntity ownerJpaEntity = new OwnerJpaEntity()
                .setUsername(username)
                .setPassword(password);
        userApplicationService.createUser(ownerJpaEntity);


        String body = objectMapper.writeValueAsString(request);


        //when
        ResultActions resultActions = mockMvc.perform(
                post("/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf())
                        .with(httpBasic(username, password))
        );

        //then
        String rawBody = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CreateInvoiceResponse response = objectMapper.readValue(rawBody, CreateInvoiceResponse.class);
        assertThat(response.getInvalidFields()).isNullOrEmpty();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getData()).isNotNull();

        InvoiceModel invoiceModel = response.getData();
        assertThat(invoiceModel.getUuid()).isNotNull();
        assertThat(invoiceModel.getInvoiceHeader()).isEqualTo(INVOICE_HEADER);
        assertThat(invoiceModel.getInvoiceDate()).isEqualTo(invoiceDate);
        assertThat(invoiceModel.getDueTime()).isEqualTo(dueTime);
        assertThat(invoiceModel.getSeller()).isNotNull();
        assertThat(invoiceModel.getBuyer()).isNotNull();
        assertThat(invoiceModel.getPaymentLink()).isNull();
        assertThat(invoiceModel.isPaid()).isFalse();
        assertThat(invoiceModel.getCurrency()).isEqualTo(EUR_CURRENCY);
        assertThat(invoiceModel.getLineItems()).isNotEmpty();

        Company actualSeller = invoiceModel.getSeller();
        assertThat(actualSeller.email()).isEqualTo(SELLER_EMAIL);
        assertThat(actualSeller.name()).isEqualTo(SELLER_COMPANY_NAME);
        assertThat(actualSeller.phoneNumber()).isEqualTo(SELLER_PHONE_NUMBER);
        assertThat(actualSeller.address()).isNotNull();

        Address actualSellerAddress = actualSeller.address();
        assertThat(actualSellerAddress.city()).isEqualTo(SELLER_CITY);
        assertThat(actualSellerAddress.country()).isEqualTo(SELLER_COUNTRY);
        assertThat(actualSellerAddress.postalCode()).isEqualTo(SELLER_POSTAL_CODE);
        assertThat(actualSellerAddress.street()).isEqualTo(SELLER_STREET);

        Company actualBuyer = invoiceModel.getBuyer();
        assertThat(actualBuyer.email()).isEqualTo(BUYER_EMAIL);
        assertThat(actualBuyer.name()).isEqualTo(BUYER_COMPANY_NAME);
        assertThat(actualBuyer.phoneNumber()).isEqualTo(BUYER_PHONE_NUMBER);
        assertThat(actualBuyer.address()).isNotNull();

        Address actualBuyerAddress = actualBuyer.address();
        assertThat(actualBuyerAddress.city()).isEqualTo(BUYER_CITY);
        assertThat(actualBuyerAddress.country()).isEqualTo(BUYER_COUNTRY);
        assertThat(actualBuyerAddress.postalCode()).isEqualTo(BUYER_POSTAL_CODE);
        assertThat(actualBuyerAddress.street()).isEqualTo(BUYER_STREET);

        Optional<Invoice> invoiceFromQuery = invoiceRepository.findByUuid(invoiceModel.getUuid());
        assertThat(invoiceFromQuery).isPresent();
        Invoice expectedInvoice = invoiceFromQuery.get();

        assertThat(invoiceModel.getUuid()).isEqualTo(expectedInvoice.getUuid());
        assertThat(invoiceModel.getInvoiceHeader()).isEqualTo(expectedInvoice.getInvoiceHeader());
        assertThat(invoiceModel.getInvoiceDate().truncatedTo(ChronoUnit.DAYS)).isEqualTo(expectedInvoice.getInvoiceDate().truncatedTo(ChronoUnit.DAYS));
        assertThat(invoiceModel.getDueTime().truncatedTo(ChronoUnit.DAYS)).isEqualTo(expectedInvoice.getDueTime().truncatedTo(ChronoUnit.DAYS));

        assertThat(invoiceModel.getSeller().name()).isEqualTo(expectedInvoice.getSeller().name());
        assertThat(invoiceModel.getSeller().phoneNumber()).isEqualTo(expectedInvoice.getSeller().phoneNumber());
        assertThat(invoiceModel.getSeller().email()).isEqualTo(expectedInvoice.getSeller().email());
        assertThat(invoiceModel.getSeller().address().street()).isEqualTo(expectedInvoice.getSeller().address().street());
        assertThat(invoiceModel.getSeller().address().city()).isEqualTo(expectedInvoice.getSeller().address().city());
        assertThat(invoiceModel.getSeller().address().postalCode()).isEqualTo(expectedInvoice.getSeller().address().postalCode());
        assertThat(invoiceModel.getSeller().address().country()).isEqualTo(expectedInvoice.getSeller().address().country());

        assertThat(invoiceModel.getBuyer().name()).isEqualTo(expectedInvoice.getBuyer().name());
        assertThat(invoiceModel.getBuyer().phoneNumber()).isEqualTo(expectedInvoice.getBuyer().phoneNumber());
        assertThat(invoiceModel.getBuyer().email()).isEqualTo(expectedInvoice.getBuyer().email());
        assertThat(invoiceModel.getBuyer().address().street()).isEqualTo(expectedInvoice.getBuyer().address().street());
        assertThat(invoiceModel.getBuyer().address().city()).isEqualTo(expectedInvoice.getBuyer().address().city());
        assertThat(invoiceModel.getBuyer().address().postalCode()).isEqualTo(expectedInvoice.getBuyer().address().postalCode());
        assertThat(invoiceModel.getBuyer().address().country()).isEqualTo(expectedInvoice.getBuyer().address().country());

        assertThat(invoiceModel.getLineItems()).hasSize(expectedInvoice.getLineItems().size());
        LineItem actualLineItem = invoiceModel.getLineItems().getFirst();
        LineItem expectedLineItem = expectedInvoice.getLineItems().getFirst();

        assertThat(actualLineItem.getDescription()).isEqualTo(expectedLineItem.getDescription());
        assertThat(actualLineItem.getAmountPerItem()).isEqualTo(expectedLineItem.getAmountPerItem());
        assertThat(actualLineItem.getTotalAmount()).isEqualTo(expectedLineItem.getTotalAmount());
        assertThat(actualLineItem.getTotalTaxAmount()).isEqualTo(expectedLineItem.getTotalTaxAmount());
        assertThat(actualLineItem.getQuantity()).isEqualTo(expectedLineItem.getQuantity());
        assertThat(actualLineItem.getTax().intValue()).isEqualTo(expectedLineItem.getTax().intValue());

        assertThat(invoiceModel.getTotalAmount()).isEqualTo(expectedInvoice.getTotalAmount());
        assertThat(invoiceModel.getTotalTaxAmount()).isEqualTo(expectedInvoice.getTotalTaxAmount());
        assertThat(invoiceModel.getPaymentLink()).isEqualTo(expectedInvoice.getPaymentLink());
        assertThat(invoiceModel.getCurrency()).isEqualTo(expectedInvoice.getCurrency());
        assertThat(invoiceModel.isPaid()).isEqualTo(expectedInvoice.isPaid());
    }
}