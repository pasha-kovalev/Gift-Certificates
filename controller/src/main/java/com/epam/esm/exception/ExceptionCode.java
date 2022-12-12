package com.epam.esm.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Stores custom exception codes
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionCode {
    public static final int HANDLER_NOT_FOUND_CODE = 40404;
    public static final int ENTITY_NOT_PRESENT_CODE = 41404;
    public static final int ENTITY_NOT_VALID_CODE = 46400;
    public static final int INVALID_QUERY_CODE = 40400;
    public static final int INVALID_INTEGRITY_CODE = 40406;
    public static final int SERVICE_UNKNOWN_EXCEPTION_CODE = 50500;
    public static final int ARGUMENT_NOT_VALID_CODE = 41400;
    public static final int FORMAT_NOT_VALID_CODE = 42400;
    public static final int DUPLICATED_ENTITY_CODE = 43400;

    public static final String OAUTH2_EXCEPTION_CODE_PREFIX = "66";
    public static final int OAUTH2_FORBIDDEN_CODE = 66403;

}
