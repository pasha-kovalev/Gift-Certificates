package com.epam.esm.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.epam.esm.controller.config.TestConfig.AUTHORIZATION_HEADER;
import static com.epam.esm.controller.config.TestConfig.BEARER_ADMIN_TOKEN;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("dev")
class OrderControllerTest {
    public static final String HAL_FORMS_JSON_TYPE = "application/prs.hal-forms+json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeAll
    public void setup() {
        wireMockServer.start();
    }

    @AfterAll
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    @Order(1)
    void testGetAllShouldReturnAllOrders() throws Exception {
        String urlTemplate = "/orders?page=1&size=10";
        String[] expectedData = {"user2", "user3", "user4", "admin"};
        String expectedType = HAL_FORMS_JSON_TYPE;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate).header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$._embedded.orderDtoList[*].user.name").value(containsInAnyOrder(expectedData)))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testGetByIdShouldReturnFoundOrderWithGivenId() throws Exception {
        String urlTemplate = "/orders/1";
        long expectedId = 1;
        String expectedType = HAL_FORMS_JSON_TYPE;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate).header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(expectedId))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testGetByUserIdShouldReturnFoundOrderWithGivenUserId() throws Exception {
        String urlTemplate = "/orders?page=1&size=1&userId=1";
        String expectedType = HAL_FORMS_JSON_TYPE;
        int expectedId = 1;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate).header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$._embedded.orderDtoList[*].id").value(expectedId))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    @Order(2)
    void testCreateNewOrderShouldReturnCreatedOrder() throws Exception {
        String urlTemplate = "/orders";
        String newOrder =
                "{\n"
                        + "    \"certificateOrdersSet\": [\n"
                        + "        {\n"
                        + "            \"certificateId\":1,\n"
                        + "            \"quantity\": 1\n"
                        + "        }\n"
                        + "    ]\n"
                        + "}";

        long expectedId = 5;
        String expectedType = HAL_FORMS_JSON_TYPE;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(post(urlTemplate)
                                .header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON).content(newOrder))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value(expectedId))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    @Order(3)
    void testDeleteByIdShouldReturnDeleteMethodNotSupported() throws Exception {
        String urlTemplate = "/orders/4";

        this.mockMvc.perform(delete(urlTemplate)
                .header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN)
        ).andExpect(status().is4xxClientError());
    }
}
