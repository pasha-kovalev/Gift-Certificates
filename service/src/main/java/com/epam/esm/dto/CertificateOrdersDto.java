package com.epam.esm.dto;

import com.epam.esm.domain.GiftCertificateOrders;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO that transfers {@link GiftCertificateOrders} data about
 */
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"price", "duration"}, allowGetters = true)
@Getter
@Setter
public class CertificateOrdersDto {
    @Positive
    private long certificateId;

    @Positive
    @Max(10)
    private int quantity;

    @Null
    private BigDecimal price;

    @Null
    private Long duration;
}
