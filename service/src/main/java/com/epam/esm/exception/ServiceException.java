package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an exception that is thrown when common service exception occurs
 */
@AllArgsConstructor
@Getter
public class ServiceException extends RuntimeException {
    /**
     * Key of message in Resource Bundle
     */
    private final String messageKey;
    /**
     * ID of entity that caused the exception
     */
    private final long entityId;
}
