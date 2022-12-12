package com.epam.esm.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents Gift Certificate entity from database
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@Audited
@Table(name = "gift_certificate")
@Entity
public class GiftCertificate implements BaseEntity {
    @Serial
    private static final long serialVersionUID = 7321606151050159057L;

    /**
     * Certificate identifier in database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Certificate name
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Certificate description
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Certificate price
     */
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * Certificate validity period in days
     */
    @Column(nullable = false)
    private Long duration;

    /**
     * Creation date of certificate
     */
    @Column(name = "create_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    /**
     * The date the certificate was last modified
     */
    @Column(name = "last_update_date")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

    /**
     * Set of Tags associated with the certificate
     */
    @Cascade(CascadeType.SAVE_UPDATE)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "gift_certificate_tags",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();
}
