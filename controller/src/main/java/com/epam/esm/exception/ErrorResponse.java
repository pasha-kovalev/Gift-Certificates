package com.epam.esm.exception;

/**
 * Represents response object that contains custom error content
 */
public record ErrorResponse(String message, int code) {

}
