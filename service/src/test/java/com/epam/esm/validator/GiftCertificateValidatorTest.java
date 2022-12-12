package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.validator.config.ValidatorTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {ValidatorTestConfig.class})
class GiftCertificateValidatorTest {
    private final GiftCertificateDto certificateDto1 =
            new GiftCertificateDto(
                    1L,
                    "name",
                    "desc",
                    BigDecimal.TEN,
                    30L,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    new HashSet<>());

    private final GiftCertificateDto certificateDto2 =
            new GiftCertificateDto(
                    1L,
                    null,
                    "desc",
                    BigDecimal.TEN,
                    30L,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    new HashSet<>());

    @Autowired
    GiftCertificateValidator validator;

    @Test
    void isValidShouldReturnTrueWhenCertificateValid() {
        Assertions.assertTrue(validator.isValid(certificateDto1));
    }

    @Test
    void isValidShouldReturnTrueWhenCertificateNotValid() {
        Assertions.assertFalse(validator.isValid(certificateDto2));
    }
}
