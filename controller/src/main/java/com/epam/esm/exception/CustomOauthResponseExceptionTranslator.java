package com.epam.esm.exception;

import com.epam.esm.locale.MessageTranslator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import static com.epam.esm.exception.ExceptionCode.OAUTH2_EXCEPTION_CODE_PREFIX;
import static com.epam.esm.exception.ResourceBundleMessageKey.OAUTH2_BAD_CLIENT;
import static com.epam.esm.exception.ResourceBundleMessageKey.OAUTH2_ERROR;
import static com.epam.esm.exception.ResourceBundleMessageKey.OAUTH2_INSUFFICIENT_SCOPE;
import static com.epam.esm.exception.ResourceBundleMessageKey.OAUTH2_INVALID_SCOPE;
import static com.epam.esm.exception.ResourceBundleMessageKey.OAUTH2_UNAUTHORIZED;
import static org.springframework.security.oauth2.common.exceptions.OAuth2Exception.INSUFFICIENT_SCOPE;
import static org.springframework.security.oauth2.common.exceptions.OAuth2Exception.INVALID_CLIENT;
import static org.springframework.security.oauth2.common.exceptions.OAuth2Exception.INVALID_SCOPE;
import static org.springframework.security.oauth2.common.exceptions.OAuth2Exception.UNAUTHORIZED_CLIENT;

/**
 * Handles {@link OAuth2Exception} exceptions
 */
@Component
public class CustomOauthResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {
    private final MessageTranslator messageTranslator;

    public CustomOauthResponseExceptionTranslator(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
        OAuth2Exception oAuth2Exception = responseEntity.getBody();
        if (oAuth2Exception != null) {
            oAuth2Exception.addAdditionalInformation("code",
                    OAUTH2_EXCEPTION_CODE_PREFIX + oAuth2Exception.getHttpErrorCode());
            setMessage(oAuth2Exception);
        }
        return new ResponseEntity<>(oAuth2Exception, responseEntity.getHeaders(), responseEntity.getStatusCode());
    }

    protected void setMessage(OAuth2Exception oAuth2Exception) {
        String messageKey = getMessageKey(oAuth2Exception);

        oAuth2Exception.addAdditionalInformation("message",
                messageTranslator.getMessageForLocale(messageKey, null));
    }

    private String getMessageKey(OAuth2Exception oAuth2Exception) {
        return switch (oAuth2Exception.getOAuth2ErrorCode()) {
            case INVALID_CLIENT -> OAUTH2_BAD_CLIENT;
            case INSUFFICIENT_SCOPE -> OAUTH2_INSUFFICIENT_SCOPE;
            case INVALID_SCOPE -> OAUTH2_INVALID_SCOPE;
            case UNAUTHORIZED_CLIENT, "unauthorized" -> OAUTH2_UNAUTHORIZED;
            default -> OAUTH2_ERROR;
        };
    }
}
