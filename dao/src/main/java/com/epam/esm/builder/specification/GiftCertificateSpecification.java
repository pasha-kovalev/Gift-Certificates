package com.epam.esm.builder.specification;

import com.epam.esm.builder.RequestQueryParameters;
import com.epam.esm.builder.SearchCriteria;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.epam.esm.dao.GiftCertificateRepository.TAGS_PARAM_NAME;
import static com.epam.esm.dao.TagRepository.NAME_FIELD_NAME;

/**
 * Interface that describes basic specification for BaseEntity
 */
@RequiredArgsConstructor
public class GiftCertificateSpecification implements Specification<GiftCertificate> {
    @NonNull
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.key().equalsIgnoreCase(RequestQueryParameters.TAG_NAME)) {
            return hasTagName(criteria.value().toString(), root, criteriaBuilder);
        }
        return switch (criteria.operation()) {
            case LIKE -> criteriaBuilder.like(root.get(criteria.key()), "%" + criteria.value().toString() + "%");
            default -> criteriaBuilder.equal(root.get(criteria.key()), criteria.value());
        };
    }

    private Predicate hasTagName(String tagName, Root<GiftCertificate> root, CriteriaBuilder cb) {
        Join<GiftCertificate, Tag> tagJoin = root.join(TAGS_PARAM_NAME);
        return cb.equal(tagJoin.get(NAME_FIELD_NAME), tagName);
    }
}
