package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO that transfers {@link com.epam.esm.domain.GiftCertificate} content
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Relation(itemRelation = "giftCertificate", collectionRelation = "giftCertificates")
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> implements BaseDto {
    /**
     * Identifier in database
     */
    @Positive
    private Long id;

    /**
     * Certificate name
     */
    @Size(min = 3, max = 45, message = "validator.giftCertificate.name.size")
    @Pattern(regexp = "^\\w[\\w\\s]+", message = "validator.giftCertificate.name")
    private String name;

    /**
     * Certificate description
     */
    @Size(min = 3, max = 300, message = "validator.giftCertificate.description.size")
    @Pattern(regexp = "^\\w[\\w\\s,.?!-]+", message = "validator.giftCertificate.description")
    private String description;

    /**
     * Certificate price
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "validator.giftCertificate.price.min")
    @Digits(integer = 5, fraction = 2, message = "validator.giftCertificate.price.format")
    private BigDecimal price;

    /**
     * Certificate validity period in days
     */
    @JsonFormat
    private long duration;

    /**
     * Certificate creation date
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;

    /**
     * The date the certificate was last modified
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;

    /**
     * Set of TagDto associated with the certificate
     */
    private Set<@Valid TagDto> tags = new HashSet<>();

    public void setTags(Set<TagDto> tags) {
        if (tags == null) {
            return;
        }
        this.tags = tags;
    }
}
