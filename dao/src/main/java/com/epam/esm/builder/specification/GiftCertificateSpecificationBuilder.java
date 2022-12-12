package com.epam.esm.builder.specification;

import com.epam.esm.builder.SearchCriteria;
import com.epam.esm.domain.GiftCertificate;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * Implementation of predicate builder for GiftCertificate
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class GiftCertificateSpecificationBuilder implements SpecificationBuilder<GiftCertificate> {
    private final List<SearchCriteria> params = new ArrayList<>();

    @Override
    public GiftCertificateSpecificationBuilder with(SearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    @Override
    public GiftCertificateSpecificationBuilder with(List<SearchCriteria> criteriaList) {
        params.addAll(criteriaList);
        return this;
    }

    @Override
    public Specification<GiftCertificate> build() {
        if (params.isEmpty()) {
            return null;
        }
        List<Specification<GiftCertificate>> specs = params.stream()
                .map(GiftCertificateSpecification::new)
                .collect(Collectors.toList());

        Specification<GiftCertificate> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}
