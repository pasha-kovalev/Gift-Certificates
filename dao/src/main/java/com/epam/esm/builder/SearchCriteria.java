package com.epam.esm.builder;

/**
 * Class represents search criteria for request
 */
public record SearchCriteria(String key, SearchOperation operation, Object value) {
}
