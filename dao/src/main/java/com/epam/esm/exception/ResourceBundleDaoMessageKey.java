package com.epam.esm.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class that stores the resource bundle keys for error messages
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceBundleDaoMessageKey {
    public static final String SORT_INVALID_PARAMETERS = "error.message.sort.invalidParameters";
    public static final String SEARCH_INVALID_OPERATOR = "error.message.search.invalidOperator";
    public static final String SEARCH_INVALID_PARAMETER = "error.message.search.invalidParam";
}
