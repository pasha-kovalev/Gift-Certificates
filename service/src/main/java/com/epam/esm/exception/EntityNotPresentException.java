package com.epam.esm.exception;

/**
 * Represents an exception that is thrown when an attempt is made to obtain non-existent entity
 */
public class EntityNotPresentException extends ServiceException {

    public EntityNotPresentException(String messageKey, long entityId) {
        super(messageKey, entityId);
    }
}
