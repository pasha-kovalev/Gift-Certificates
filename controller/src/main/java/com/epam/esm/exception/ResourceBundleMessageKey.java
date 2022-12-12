package com.epam.esm.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class that stores the resource bundle keys for error messages
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceBundleMessageKey {
    public static final String FORMAT_NOT_VALID = "error.message.format.notValid";
    public static final String MISMATCH_INPUT = "error.message.format.mismatch";

    public static final String ARGUMENT_NOT_VALID = "error.message.argument.notValid";
    public static final String DUPLICATED_ENTITY = "error.message.entity.duplicate";
    public static final String HANDLER_NOT_FOUND = "error.message.handler.notFound";
    public static final String INVALID_INTEGRITY = "error.message.sql.integrity.invalid";

    public static final String OAUTH2_ACCESS_DENIES = "error.message.oauth.accessDenied";
    public static final String OAUTH2_BAD_CLIENT = "error.message.oauth.badClient";
    public static final String OAUTH2_INSUFFICIENT_SCOPE = "error.message.oauth.insufficientScope";
    public static final String OAUTH2_CLIENT_AUTH = "error.message.oauth.client";
    public static final String OAUTH2_INVALID_SCOPE = "error.message.oauth.invalidScope";
    public static final String OAUTH2_UNAUTHORIZED = "error.message.oauth.unauthorized";
    public static final String OAUTH2_ERROR = "error.message.oauth.common";
}
