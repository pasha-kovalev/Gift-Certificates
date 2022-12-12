package com.epam.esm.validator.config;

import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorTestConfig {

    @Bean
    public GiftCertificateValidator validator() {
        return new GiftCertificateValidator();
    }
}
