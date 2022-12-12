package com.epam.esm.dao;

import com.epam.esm.domain.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository extends BaseRepository<GiftCertificate>, JpaSpecificationExecutor<GiftCertificate> {
    String TAGS_PARAM_NAME = "tags";

    Page<GiftCertificate> findAll(Specification<GiftCertificate> spec, Pageable pageable);

    Page<GiftCertificate> findAll(Pageable pageable);
}

