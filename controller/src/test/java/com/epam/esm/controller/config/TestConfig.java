package com.epam.esm.controller.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
public class TestConfig {
    public static final int AUTH_SERVER_PORT_NUMBER = 9002;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_USER_TOKEN = "Bearer user";
    public static final String BEARER_ADMIN_TOKEN = "Bearer admin";

    @Bean
    WireMockServer wireMockServer() {
        WireMockServer server = new WireMockServer(options().port(AUTH_SERVER_PORT_NUMBER));
        server.stubFor(WireMock.any(urlPathMatching("/oauth/.*")).willReturn(aResponse().withStatus(200)));
        configureAdminStub(server);
        configureUserStub(server);
        return server;
    }

    private void configureUserStub(WireMockServer server) {
        server.stubFor(WireMock.post(urlPathMatching("/oauth/check_token"))
                .withRequestBody(containing("user"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                            "exp": 1657633333,
                                            "user_name": "user",
                                            "authorities": [
                                                "ROLE_USER"
                                            ],
                                            "jti": "d8ab080f-0bdc-45f3-b12e-72ed7d517f0e",
                                            "client_id": "postman",
                                            "scope": [
                                                "read"
                                            ]
                                        }
                                        """)));
    }

    private void configureAdminStub(WireMockServer server) {
        server.stubFor(WireMock.post(urlPathMatching("/oauth/check_token"))
                .withRequestBody(containing("admin"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        {
                                            "exp": 1657633333,
                                            "user_name": "admin",
                                            "authorities": [
                                                "ROLE_ADMIN"
                                            ],
                                            "jti": "d8ab080f-0bdc-45f3-b12e-72ed7d517f0e",
                                            "client_id": "postman",
                                            "scope": [
                                                "write"
                                            ]
                                        }
                                        """)));
    }


}
