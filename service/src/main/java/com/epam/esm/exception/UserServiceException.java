package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an exception that is thrown when an attempt is made to obtain non-existent entity
 */
@AllArgsConstructor
@Getter
public class UserServiceException extends RuntimeException {
    /**
     * Key of message in Resource Bundle
     */
    private final String messageKey;
    /**
     * ID of entity that caused the exception
     */
    private final String userName;
}
