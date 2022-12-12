package com.epam.esm.controller;

import com.epam.esm.service.GiftCertificateService;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("dev")
class GiftCertificateControllerTest {
    public static final String HAL_FORMS_JSON_TYPE = "application/prs.hal-forms+json";
    public static final String JSON_TYPE = "application/json";

    @Autowired
    GiftCertificateService certificateService;
    @Autowired
    GiftCertificateController certificateController;

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
    void testGetAllShouldReturnAllCertificates() throws Exception {
        String urlTemplate = "/gift-certificates?page=1&size=10";
        String[] expectedData = {"c1", "c2"};
        String expectedType = HAL_FORMS_JSON_TYPE;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$._embedded.giftCertificates[*].name").value(containsInAnyOrder(expectedData)))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testGetAllWithSearchAndFilterShouldReturnErrorCodeWhenIncorrectSortValues()
            throws Exception {
        String urlTemplate = "/gift-certificates?page=1&size=10sort=create_date,--name";
        String expectedType = JSON_TYPE;

        MvcResult actualMvcResult =
                this.mockMvc.perform(get(urlTemplate)).andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    void testGetAllWithSearchAndFilterShouldReturnCorrectData() throws Exception {
        String urlTemplate = "/gift-certificates?page=1&size=10&sort=name&search=name~c,tagName:mjc";
        String expectedCertificateName = "c1";

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate))
                        .andExpect(jsonPath("$._embedded.giftCertificates[*].name").value(expectedCertificateName))
                        .andReturn();
        assertEquals(HAL_FORMS_JSON_TYPE, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testGetAllWithSearchAndFilterShouldReturnErrorCodeWhenIncorrectSearchOperator()
            throws Exception {
        String urlTemplate = "/gift-certificates?page=1&size=10search=name%c23&sort=create_date,-name";

        MvcResult actualMvcResult =
                this.mockMvc.perform(get(urlTemplate)).andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    void testGetAllWithSearchAndFilterShouldReturnErrorCodeWhenIncorrectSearchParam()
            throws Exception {
        String urlTemplate = "/gift-certificates?page=1&size=10search=surname~c&sort=create_date,-name";

        MvcResult actualMvcResult =
                this.mockMvc.perform(get(urlTemplate)).andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    void testUpdateShouldReturnUpdatedEntity() throws Exception {
        String urlTemplate = "/gift-certificates/1";
        String updateData = "{\"duration\": 53}";
        int expectedDuration = 53;

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(patch(urlTemplate)
                                .header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON).content(updateData))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.duration").value(expectedDuration))
                        .andReturn();
        assertEquals(HAL_FORMS_JSON_TYPE, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testGetByIdShouldReturnFoundCertificatesWithGivenId() throws Exception {
        int expectedId = 1;
        String urlTemplate = "/gift-certificates/1";

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(expectedId))
                        .andReturn();
        assertEquals(HAL_FORMS_JSON_TYPE, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testGetByIdShouldReturnShouldReturnErrorCodeWhenNotFound() throws Exception {
        String urlTemplate = "/gift-certificates/0";

        MvcResult actualMvcResult =
                this.mockMvc.perform(get(urlTemplate)).andExpect(status().is4xxClientError()).andReturn();
        assertEquals(JSON_TYPE, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testCreateNewCertificateShouldReturnCreatedCertificate() throws Exception {
        String urlTemplate = "/gift-certificates";
        String newTag =
                "{\"name\": \"Unbranded Steel Keyboard\",\n"
                        + "    \"description\": \"conglomeration moratorium Music Missouri leading-edge\",\n"
                        + "    \"price\": 2261.00,\n"
                        + "    \"duration\": 9, \"tags\": [{\"name\": \"new\"}]}";
        String expectedData = "{'id':4, 'tags': [{'name': 'new'}]}";

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(post(urlTemplate)
                                .header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN)
                                .contentType(MediaType.APPLICATION_JSON).content(newTag))
                        .andExpect(status().isCreated())
                        .andExpect(content().json(expectedData))
                        .andReturn();
        assertEquals(HAL_FORMS_JSON_TYPE, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testDeleteByIdShouldReturnNoContent() throws Exception {
        String urlTemplate = "/gift-certificates/3";

        this.mockMvc.perform(delete(urlTemplate)
                        .header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void testUntieTagShouldReturnErrorCodeWhenTagNotTied() throws Exception {
        String urlTemplate = "/gift-certificates/1/tags/3";

        this.mockMvc
                .perform(delete(urlTemplate)
                        .header(AUTHORIZATION_HEADER, BEARER_ADMIN_TOKEN))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}
