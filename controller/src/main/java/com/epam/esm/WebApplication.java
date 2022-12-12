package com.epam.esm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Gift Certificates API", version = "0.0.1", description = "Gift Certificates selling app"))
@SecurityScheme(name = "oauth2", type = SecuritySchemeType.OAUTH2, scheme = "bearer", bearerFormat = "jwt",
        in = SecuritySchemeIn.HEADER,
        flows = @OAuthFlows(implicit =
        @OAuthFlow(authorizationUrl = "${springdoc.swagger-ui.oauth.auth-url}", tokenUrl = "${springdoc.swagger-ui.oauth.token-url}",
                scopes = {@OAuthScope(name = "read"), @OAuthScope(name = "write")}
        ))
)
@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
