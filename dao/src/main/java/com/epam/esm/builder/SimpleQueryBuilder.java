package com.epam.esm.builder;

import com.epam.esm.builder.specification.SpecificationBuilder;
import com.epam.esm.domain.BaseEntity;
import com.epam.esm.exception.InvalidQueryStringException;
import com.google.common.base.CaseFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ResourceBundleDaoMessageKey.SEARCH_INVALID_OPERATOR;
import static com.epam.esm.exception.ResourceBundleDaoMessageKey.SORT_INVALID_PARAMETERS;

/**
 * Simple implementation of query builder
 *
 * @param <T> the type parameter
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleQueryBuilder<T extends BaseEntity> implements QueryBuilder<T> {
    private static final String REVERSE_SORT_MARKER_REGEX = "^-";
    private static final String SORT_VALUES_DELIMITER = ",";
    private static final String REVERSE_SORT_MARKER = "-";
    private static final String ASC_SORT_MARKER = "asc";
    private static final String DESC_SORT_MARKER = "desc";

    /**
     * Creates specification based on search parameters values
     *
     * @param search                  the search parameter value from request
     * @param fieldsAvailableToSearch the set of fields available to search
     * @param specificationBuilder    specification builder
     * @return search specification
     */
    public Specification<T> buildSearchSpecification(
            String search,
            Set<String> fieldsAvailableToSearch,
            SpecificationBuilder<T> specificationBuilder) {
        if (search == null) {
            return null;
        }
        List<SearchCriteria> criteriaList = QueryParser.parseSearchQueryStringParameters(search);
        checkSearchParams(criteriaList, fieldsAvailableToSearch);
        return specificationBuilder.with(criteriaList).build();
    }

    /**
     * Creates {@link org.springframework.data.domain.Sort} based on sort parameters values
     *
     * @param sort                     the sort parameter value from request
     * @param fieldsAvailableToOrderBy the set of fields available to sort by
     * @return {@link org.springframework.data.domain.Sort}
     */
    public Sort buildOrderList(String sort, Set<String> fieldsAvailableToOrderBy) {
        if (sort == null) {
            return Sort.unsorted();
        }
        fieldsAvailableToOrderBy = convertSnakeCaseToCamel(fieldsAvailableToOrderBy);
        List<String> fieldsToSort = processSortString(sort);
        List<String> valuesWithoutReverseSortMarker = getValuesWithoutReverseSortMarker(fieldsToSort);
        if (!fieldsAvailableToOrderBy.containsAll(valuesWithoutReverseSortMarker)) {
            valuesWithoutReverseSortMarker.removeAll(fieldsAvailableToOrderBy);
            throw new InvalidQueryStringException(
                    SORT_INVALID_PARAMETERS, valuesWithoutReverseSortMarker.toString());
        }
        Map<String, Boolean> fieldToIsReverseOrder = getFieldToIsReverseOrderMap(fieldsToSort);
        return createOrderByList(fieldToIsReverseOrder);
    }

    private List<String> getValuesWithoutReverseSortMarker(List<String> list) {
        return list.stream()
                .map(v -> v.replaceFirst(REVERSE_SORT_MARKER_REGEX, ""))
                .collect(Collectors.toList());
    }

    private Map<String, Boolean> getFieldToIsReverseOrderMap(List<String> fieldsToSort) {
        return fieldsToSort.stream()
                .collect(
                        Collectors.toMap(
                                v -> v.replaceFirst(REVERSE_SORT_MARKER_REGEX, ""),
                                v -> v.startsWith(REVERSE_SORT_MARKER),
                                (v1, v2) -> v1,
                                LinkedHashMap::new));
    }

    private Sort createOrderByList(Map<String, Boolean> fieldToIsReverseOrder) {
        List<Order> orders = fieldToIsReverseOrder.entrySet()
                .stream()
                .map(e -> Boolean.TRUE.equals(e.getValue())
                        ? new Order(Sort.Direction.DESC, e.getKey())
                        : new Order(Sort.Direction.ASC, e.getKey())
                )
                .toList();
        return Sort.by(orders);
    }

    private void checkSearchParams(List<SearchCriteria> criteriaList, Set<String> fieldsAvailableToSearch) {
        List<String> fieldsToSearch =
                criteriaList.stream().map(SearchCriteria::key).collect(Collectors.toList());
        if (!fieldsAvailableToSearch.containsAll(fieldsToSearch)) {
            fieldsToSearch.removeAll(fieldsAvailableToSearch);
            throw new InvalidQueryStringException(SEARCH_INVALID_OPERATOR, fieldsToSearch.toString());
        }
    }

    private List<String> processSortString(String sort) {
        String[] fieldsToSort = sort.split(SORT_VALUES_DELIMITER);
        for (int i = 1; i < fieldsToSort.length; i++) {
            if (fieldsToSort[i].equalsIgnoreCase(DESC_SORT_MARKER)) {
                fieldsToSort[i - 1] = REVERSE_SORT_MARKER + fieldsToSort[i - 1];
            }
        }
        return Arrays.stream(fieldsToSort)
                .filter(e -> !(e.equalsIgnoreCase(ASC_SORT_MARKER) || e.equalsIgnoreCase(DESC_SORT_MARKER)))
                .collect(Collectors.toList());
    }

    private Set<String> convertSnakeCaseToCamel(Set<String> fields) {
        return fields.stream()
                .map(f -> CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, f))
                .collect(Collectors.toSet());
    }
}
