package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;

/**
 * Gift Certificate validator.
 */
@Component
public class GiftCertificateValidator {

    /**
     * Checks if Gift Certificate contains null values where it is inappropriate
     *
     * @param dto Gift Certificate
     * @return is certificate valid
     */
    public boolean isValid(GiftCertificateDto dto) {
        return ((dto.getName() != null)
                && (dto.getDescription() != null)
                && (dto.getDuration() > 0)
                && (dto.getPrice() != null));
    }
}
