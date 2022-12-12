package com.epam.esm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.math.BigDecimal;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * Represents GiftCertificateOrders entity from database
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "gift_certificate_orders")
@Audited
@Entity
public class GiftCertificateOrders {
    /**
     * GiftCertificateOrders identifier in database
     */
    @EmbeddedId
    private GiftCertificateOrdersKey id = new GiftCertificateOrdersKey();

    /**
     * Order joined by order id
     */
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonIgnore
    @Audited(targetAuditMode = NOT_AUDITED)
    private Order order;

    /**
     * Gift Certificate joined by order id
     */
    @ManyToOne
    @MapsId("giftCertificateId")
    @JoinColumn(name = "gift_certificate_id")
    @Audited(targetAuditMode = NOT_AUDITED)
    private GiftCertificate giftCertificate;

    /**
     * Quantity of gift certificate in order
     */
    @Column(name = "quantity", nullable = false)
    private int quantity;

    /**
     * Certificate price at the time of purchase
     */
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * Certificate duration at the time of purchase
     */
    @Column(name = "duration", nullable = false)
    private long duration;
}
