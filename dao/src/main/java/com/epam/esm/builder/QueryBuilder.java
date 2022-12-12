package com.epam.esm.builder;

import com.epam.esm.domain.BaseEntity;
import com.epam.esm.domain.GiftCertificate;

/**
 * Interface that describes basic query builders and provides them
 */
public interface QueryBuilder<T extends BaseEntity> {
    static SimpleQueryBuilder<GiftCertificate> createGiftCertificateBuilder() {
        return new SimpleQueryBuilder<>();
    }
}
