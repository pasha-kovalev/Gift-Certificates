package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Configuration for the web application, adds message converter and locale resolver with
 * ResourceBundleMessageSource
 */
@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class WebConfig extends AcceptHeaderLocaleResolver {

    private static final List<Locale> LOCALES = Arrays.asList(new Locale("ru"), new Locale("en"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return ((headerLang == null) || (headerLang.isEmpty()))
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        final String RESOURCE_BUNDLE_BASENAME = "messages";
        final String ENCODING = "UTF-8";
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename(RESOURCE_BUNDLE_BASENAME);
        rs.setDefaultEncoding(ENCODING);
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    PageableHandlerMethodArgumentResolverCustomizer pageableResolverCustomizer() {
        return pageableResolver -> pageableResolver.setOneIndexedParameters(true);
    }

}
