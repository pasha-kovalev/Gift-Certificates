package com.epam.esm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents GiftCertificateOrders key
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GiftCertificateOrdersKey implements Serializable {
    /**
     * Order id
     */
    private Long orderId;

    /**
     * Gift certificate id
     */
    private Long giftCertificateId;
}
