package com.epam.esm.security;

import com.epam.esm.domain.Role;
import com.epam.esm.exception.CustomOauthAccessDeniedHandler;
import com.epam.esm.exception.CustomOauthResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * Configuration for the resource server application, configures request authorization, token services
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private static final String OAUTH_2_HAS_SCOPE_WRITE = "#oauth2.hasScope('write')";

    private final CustomOauthResponseExceptionTranslator exceptionTranslator;
    private final CustomOauthAccessDeniedHandler accessDeniedHandler;

    @Value("${server.jwt.token-info-uri}")
    private String tokenInfoUri;

    @Value("${server.jwt.client-id}")
    private String clientId;

    @Value("${server.jwt.client-secret}")
    private String clientSecret;

    @Autowired
    public ResourceServerConfiguration(CustomOauthResponseExceptionTranslator exceptionTranslator, CustomOauthAccessDeniedHandler accessDeniedHandler) {
        this.exceptionTranslator = exceptionTranslator;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/gift-certificates/**", "/login").permitAll()
                .antMatchers("/users/signup", "/actuator/**", "/v3/**", "/swagger**/**", "gift-certificates").permitAll()
                .antMatchers(HttpMethod.GET).hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
                .antMatchers(HttpMethod.POST, "/orders").hasAnyRole(Role.USER.toString(), Role.ADMIN.toString())
                .antMatchers(HttpMethod.POST).access(OAUTH_2_HAS_SCOPE_WRITE)
                .antMatchers(HttpMethod.PATCH).access(OAUTH_2_HAS_SCOPE_WRITE)
                .antMatchers(HttpMethod.DELETE).access(OAUTH_2_HAS_SCOPE_WRITE)
                .anyRequest().hasRole(Role.ADMIN.toString());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().and()
                .csrf().disable();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setExceptionTranslator(exceptionTranslator);
        config.accessDeniedHandler(accessDeniedHandler);
        config.tokenServices(tokenServices());
        config.authenticationEntryPoint(authenticationEntryPoint);
    }

    @Bean
    @Primary
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecret);
        tokenServices.setCheckTokenEndpointUrl(tokenInfoUri);
        return tokenServices;
    }


}