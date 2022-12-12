package com.epam.esm.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Class for storing request query parameters
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestParams {
    public static final String USER_ID_PARAM = "userId";
    public static final String SEARCH_PARAM = "search";
    public static final String SORT_PARAM = "sort";
}
