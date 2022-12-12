package com.epam.esm.builder;

import com.epam.esm.exception.InvalidQueryStringException;

import static com.epam.esm.exception.ResourceBundleDaoMessageKey.SEARCH_INVALID_OPERATOR;

/**
 * Class represents available search operations
 */
public enum SearchOperation {
    EQUALITY,
    LIKE;

    static SearchOperation getSimpleOperation(String input) {
        return switch (input) {
            case ":" -> EQUALITY;
            case "~" -> LIKE;
            default -> throw new InvalidQueryStringException(SEARCH_INVALID_OPERATOR, input);
        };
    }
}
