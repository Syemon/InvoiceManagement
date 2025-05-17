package com.syemon.invoicemanagement.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syemon.invoicemanagement.TestcontainersConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)

class InvoiceRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

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
}