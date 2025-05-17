package com.syemon.invoicemanagement.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syemon.invoicemanagement.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
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

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf())
        );

        //then
        resultActions
                .andExpect(status().isBadRequest());


    }
}