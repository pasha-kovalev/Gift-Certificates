package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An exception that occurs when request contains invalid query string
 */
@AllArgsConstructor
@Getter
public class InvalidQueryStringException extends RuntimeException {
    private final String messageKey;
    private final String queryPart;
}
