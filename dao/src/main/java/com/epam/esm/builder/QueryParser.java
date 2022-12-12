package com.epam.esm.builder;

import com.epam.esm.exception.InvalidQueryStringException;
import com.epam.esm.exception.ResourceBundleDaoMessageKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses query string parameters.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueryParser {
    private static final String SEARCH_REGEX = "(\\w+?)([:~])([\\w\\s]+),";
    private static final Pattern searchPattern =
            Pattern.compile(SEARCH_REGEX, Pattern.UNICODE_CHARACTER_CLASS);

    private static final int INDEX_OF_KEY_MATCH = 1;
    private static final int INDEX_OF_OPERATION_MATCH = 2;
    private static final int INDEX_OF_VALUE_MATCH = 3;

    /**
     * Parse search query string parameters
     *
     * @param search the search param value
     * @return the list of parsed parameters
     */
    public static List<SearchCriteria> parseSearchQueryStringParameters(String search) {
        if (search == null || search.isEmpty()) {
            return Collections.emptyList();
        }
        List<SearchCriteria> result = new ArrayList<>();

        Matcher matcher = searchPattern.matcher(search + ",");
        boolean foundFlag = false;
        while (matcher.find()) {
            foundFlag = true;
            SearchOperation operation =
                    SearchOperation.getSimpleOperation(matcher.group(INDEX_OF_OPERATION_MATCH));
            result.add(
                    new SearchCriteria(
                            matcher.group(INDEX_OF_KEY_MATCH), operation, matcher.group(INDEX_OF_VALUE_MATCH)));
        }
        if (!foundFlag) {
            throw new InvalidQueryStringException(
                    ResourceBundleDaoMessageKey.SEARCH_INVALID_OPERATOR, search);
        }
        return result;
    }
}
