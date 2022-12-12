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
import static com.epam.esm.controller.config.TestConfig.BEARER_USER_TOKEN;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("dev")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WireMockServer mockServer;

    @BeforeAll
    public void setup() {
        mockServer.start();
    }

    @AfterAll
    public void teardown() {
        mockServer.stop();
    }

    @Test
    @Order(1)
    void testGetAllShouldReturnAllUsers() throws Exception {
        String urlTemplate = "/users?page=1&size=10";
        String[] expectedData = {"admin", "user2", "user3", "user4"};
        String expectedType = "application/prs.hal-forms+json";

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate).header(AUTHORIZATION_HEADER, BEARER_USER_TOKEN))
                        .andExpect(status().isOk())
                        .andExpect(
                                jsonPath("$._embedded.userDtoList[*].name").value(containsInAnyOrder(expectedData)))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    void testGetByIdShouldReturnFoundUserWithGivenId() throws Exception {
        String urlTemplate = "/users/1";
        String expectedData =
                "{'id':1,'name':'admin','role':'ADMIN','_links':{'self':{'href':'http://localhost/users/1'}}}";
        String expectedType = "application/prs.hal-forms+json";

        MvcResult actualMvcResult =
                this.mockMvc
                        .perform(get(urlTemplate).header(AUTHORIZATION_HEADER, BEARER_USER_TOKEN))
                        .andExpect(status().isOk())
                        .andExpect(content().json(expectedData))
                        .andReturn();
        assertEquals(expectedType, actualMvcResult.getResponse().getContentType());
    }

    @Test
    @Order(2)
    void testSignUpShouldReturnCreatedUser() throws Exception {
        String urlTemplate = "/users/signup";
        String newUser = "{\n" +
                "    \"username\":\"NewUser\",\n" +
                "    \"password\":\"pass\"\n" +
                "}";

        this.mockMvc
                .perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON).content(newUser))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void testSignUpWhenUserExistsShouldReturnErrorCode() throws Exception {
        String urlTemplate = "/users/signup";
        String newUser = "{\n" +
                "    \"username\":\"admin\",\n" +
                "    \"password\":\"pass\"\n" +
                "}";

        this.mockMvc
                .perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON).content(newUser))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}
