package com.epam.esm.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Retrieves message from Resource Bundle by its bundle key
 */
@Component
public class MessageTranslator {
    private final ResourceBundleMessageSource messageSource;

    @Autowired
    MessageTranslator(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Gets message from resource bundle with locale value from LocaleContextHolder
     *
     * @param messageKey key of message
     * @param args       arguments of message
     * @return message
     */
    public String getMessageForLocale(String messageKey, Object[] args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }

    /**
     * Gets message from resource bundle with default locale value
     *
     * @param messageKey key of message
     * @param args       arguments of message
     * @return message
     */
    public String getMessageForDefaultLocale(String messageKey, Object[] args) {
        return messageSource.getMessage(messageKey, args, Locale.getDefault());
    }
}
