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
import static com.epam.esm.controller.config.TestConfig.BEARER_USER_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("dev")
class TagControllerTest {

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
    void testGetAllShouldReturnAllTags() throws Exception {
        String urlTemplate = "/tags?page=1&size=10";
        String expectedData = "mjc";
        String expectedType = HAL_FORMS_JSON_TYPE;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate).header(AUTHORIZATION_HEADER, BEARER_USER_TOKEN))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$._embedded.tags[0].name").value(expectedData))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    @Order(2)
    void testGetByIdShouldReturnFoundTagWithGivenId() throws Exception {
        String urlTemplate = "/tags/1";
        String expectedData = "{'id':1,'name':'mjc'}";
        String expectedType = HAL_FORMS_JSON_TYPE;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate).header(AUTHORIZATION_HEADER, BEARER_USER_TOKEN))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectedData))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    @Order(3)
    void testCreateNewTagShouldReturnCreatedTag() throws Exception {
        String urlTemplate = "/tags";
        String newTag = "{\"name\": \"preventivetag\"}";
        String expectedData = "{'name':'preventivetag'}";
        String expectedType = HAL_FORMS_JSON_TYPE;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(post(urlTemplate)
                                .header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON).content(newTag))
                        .andExpect(status().isCreated())
                        .andExpect(content().json(expectedData))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    @Order(4)
    void testDeleteByIdShouldReturnNoContent() throws Exception {
        String urlTemplate = "/tags/4";

        this.mockMvc.perform(delete(urlTemplate)
                .header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN)
        ).andExpect(status().isNoContent());
    }
}
