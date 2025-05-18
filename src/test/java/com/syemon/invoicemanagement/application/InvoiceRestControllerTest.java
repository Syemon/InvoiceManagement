package com.syemon.invoicemanagement.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syemon.invoicemanagement.TestcontainersConfiguration;
import com.syemon.invoicemanagement.domain.Address;
import com.syemon.invoicemanagement.domain.Company;
import com.syemon.invoicemanagement.domain.DocumentType;
import com.syemon.invoicemanagement.domain.Invoice;
import com.syemon.invoicemanagement.domain.InvoiceRepository;
import com.syemon.invoicemanagement.domain.LineItem;
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
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Test
    void create_shouldReturn400_whenNotValid() throws Exception {
        //given
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
        Company emptyCompany = new Company(null, null, null, null);

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

        String body = objectMapper.writeValueAsString(
                new CreateInvoiceRequest(
                        DocumentType.INVOICE,
                        OffsetDateTime.now(),
                        OffsetDateTime.now().plusMonths(1),
                        buyer,
                        buyer,
                        List.of(
                            new CreateLineItemRequest(
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

        List<CreateLineItemRequest> lineItemsCommand = List.of(
                new CreateLineItemRequest(
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

        String body = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf())
        );

        //then
        String rawBody = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CreateInvoiceResponse response = objectMapper.readValue(rawBody, CreateInvoiceResponse.class);
        assertThat(response.getInvalidFields()).isNullOrEmpty();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getData()).isNotNull();

        Invoice invoice = response.getData();
        assertThat(invoice.getUuid()).isNotNull();
        assertThat(invoice.getInvoiceHeader()).isEqualTo(INVOICE_HEADER);
        assertThat(invoice.getInvoiceDate()).isEqualTo(invoiceDate);
        assertThat(invoice.getDueTime()).isEqualTo(dueTime);
        assertThat(invoice.getSeller()).isNotNull();
        assertThat(invoice.getBuyer()).isNotNull();
        assertThat(invoice.getPaymentLink()).isNull();
        assertThat(invoice.isPaid()).isFalse();
        assertThat(invoice.getCurrency()).isEqualTo(EUR_CURRENCY);
        assertThat(invoice.getLineItems()).isNotEmpty();

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

        Optional<Invoice> invoiceFromQuery = invoiceRepository.findByUuid(invoice.getUuid());
        assertThat(invoiceFromQuery).isPresent();
        Invoice expectedInvoice = invoiceFromQuery.get();

        assertThat(invoice.getUuid()).isEqualTo(expectedInvoice.getUuid());
        assertThat(invoice.getInvoiceHeader()).isEqualTo(expectedInvoice.getInvoiceHeader());
        assertThat(invoice.getInvoiceDate()).isEqualTo(expectedInvoice.getInvoiceDate());
        assertThat(invoice.getDueTime()).isEqualTo(expectedInvoice.getDueTime());

        assertThat(invoice.getSeller().name()).isEqualTo(expectedInvoice.getSeller().name());
        assertThat(invoice.getSeller().phoneNumber()).isEqualTo(expectedInvoice.getSeller().phoneNumber());
        assertThat(invoice.getSeller().email()).isEqualTo(expectedInvoice.getSeller().email());
        assertThat(invoice.getSeller().address().street()).isEqualTo(expectedInvoice.getSeller().address().street());
        assertThat(invoice.getSeller().address().city()).isEqualTo(expectedInvoice.getSeller().address().city());
        assertThat(invoice.getSeller().address().postalCode()).isEqualTo(expectedInvoice.getSeller().address().postalCode());
        assertThat(invoice.getSeller().address().country()).isEqualTo(expectedInvoice.getSeller().address().country());

        assertThat(invoice.getBuyer().name()).isEqualTo(expectedInvoice.getBuyer().name());
        assertThat(invoice.getBuyer().phoneNumber()).isEqualTo(expectedInvoice.getBuyer().phoneNumber());
        assertThat(invoice.getBuyer().email()).isEqualTo(expectedInvoice.getBuyer().email());
        assertThat(invoice.getBuyer().address().street()).isEqualTo(expectedInvoice.getBuyer().address().street());
        assertThat(invoice.getBuyer().address().city()).isEqualTo(expectedInvoice.getBuyer().address().city());
        assertThat(invoice.getBuyer().address().postalCode()).isEqualTo(expectedInvoice.getBuyer().address().postalCode());
        assertThat(invoice.getBuyer().address().country()).isEqualTo(expectedInvoice.getBuyer().address().country());

        assertThat(invoice.getLineItems()).hasSize(expectedInvoice.getLineItems().size());
        LineItem actualLineItem = invoice.getLineItems().getFirst();
        LineItem expectedLineItem = expectedInvoice.getLineItems().getFirst();

        assertThat(actualLineItem.getDescription()).isEqualTo(expectedLineItem.getDescription());
        assertThat(actualLineItem.getAmountPerItem()).isEqualTo(expectedLineItem.getAmountPerItem());
        assertThat(actualLineItem.getTotalAmount()).isEqualTo(expectedLineItem.getTotalAmount());
        assertThat(actualLineItem.getTotalTaxAmount()).isEqualTo(expectedLineItem.getTotalTaxAmount());
        assertThat(actualLineItem.getQuantity()).isEqualTo(expectedLineItem.getQuantity());
        assertThat(actualLineItem.getTax().intValue()).isEqualTo(expectedLineItem.getTax().intValue());

        assertThat(invoice.getTotalAmount()).isEqualTo(expectedInvoice.getTotalAmount());
        assertThat(invoice.getTotalTaxAmount()).isEqualTo(expectedInvoice.getTotalTaxAmount());
        assertThat(invoice.getPaymentLink()).isEqualTo(expectedInvoice.getPaymentLink());
        assertThat(invoice.getCurrency()).isEqualTo(expectedInvoice.getCurrency());
        assertThat(invoice.isPaid()).isEqualTo(expectedInvoice.isPaid());
    }
}