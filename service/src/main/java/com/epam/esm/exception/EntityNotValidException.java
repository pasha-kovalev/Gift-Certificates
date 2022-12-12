package com.epam.esm.exception;

/**
 * An exception that occurs when request contains invalid entity parameters
 */
public class EntityNotValidException extends ServiceException {
    public EntityNotValidException(String messageKey, long entityId) {
        super(messageKey, entityId);
    }
}
