package com.epam.esm.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class that stores the resource bundle keys for error messages
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceBundleServiceErrorMessageKey {
    public static final String TAG_NOT_PRESENT = "error.message.not.found.tag";
    public static final String TAG_NOT_PRESENT_FOR_CERTIFICATE = "error.message.not.found.giftCertificate.tag";

    public static final String ORDER_NOT_PRESENT = "error.message.not.found.order";
    public static final String USER_NOT_PRESENT = "error.message.not.found.user";
    public static final String USER_EXISTS = "error.message.user.exists";
    public static final String GIFT_CERTIFICATE_NOT_PRESENT =
            "error.message.not.found.giftCertificate";
    public static final String ENTITY_INVALID_UPDATE = "error.message.update.invalidParameters";

    public static final String ENTITY_INVALID_CREATE = "error.message.entity.invalidCreate";
    public static final String NOT_FOUND_TAG_MOST_USED = "error.message.not.found.tag.mostUsed";
}
