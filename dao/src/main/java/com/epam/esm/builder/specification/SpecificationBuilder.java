package com.epam.esm.builder.specification;

import com.epam.esm.builder.SearchCriteria;
import com.epam.esm.domain.BaseEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Interface that describes basic specification builder for BaseEntity and provides them
 *
 * @param <T> the type parameter
 */
public interface SpecificationBuilder<T extends BaseEntity> {

    /**
     * Adds search criteria to builder
     *
     * @param criteria the criteria
     * @return the predicate builder
     */
    SpecificationBuilder<T> with(SearchCriteria criteria);

    /**
     * Adds search criteria list to builder
     *
     * @param criteriaList the criteria list
     * @return the predicate builder
     */
    SpecificationBuilder<T> with(List<SearchCriteria> criteriaList);

    /**
     * Builds predicates
     *
     * @return the list of built predicates
     */
    Specification<T> build();
}
