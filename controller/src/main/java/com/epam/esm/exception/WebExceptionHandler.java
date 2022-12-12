package com.epam.esm.exception;

import com.epam.esm.locale.MessageTranslator;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import static com.epam.esm.exception.ExceptionCode.ARGUMENT_NOT_VALID_CODE;
import static com.epam.esm.exception.ExceptionCode.DUPLICATED_ENTITY_CODE;
import static com.epam.esm.exception.ExceptionCode.ENTITY_NOT_PRESENT_CODE;
import static com.epam.esm.exception.ExceptionCode.ENTITY_NOT_VALID_CODE;
import static com.epam.esm.exception.ExceptionCode.FORMAT_NOT_VALID_CODE;
import static com.epam.esm.exception.ExceptionCode.HANDLER_NOT_FOUND_CODE;
import static com.epam.esm.exception.ExceptionCode.INVALID_INTEGRITY_CODE;
import static com.epam.esm.exception.ExceptionCode.INVALID_QUERY_CODE;
import static com.epam.esm.exception.ExceptionCode.SERVICE_UNKNOWN_EXCEPTION_CODE;
import static com.epam.esm.exception.ResourceBundleMessageKey.ARGUMENT_NOT_VALID;
import static com.epam.esm.exception.ResourceBundleMessageKey.DUPLICATED_ENTITY;
import static com.epam.esm.exception.ResourceBundleMessageKey.FORMAT_NOT_VALID;
import static com.epam.esm.exception.ResourceBundleMessageKey.HANDLER_NOT_FOUND;
import static com.epam.esm.exception.ResourceBundleMessageKey.INVALID_INTEGRITY;
import static com.epam.esm.exception.ResourceBundleMessageKey.MISMATCH_INPUT;

/**
 * Handles exceptions in controllers
 */
@Log4j2
@ComponentScan("com.epam.esm")
@ControllerAdvice
public class WebExceptionHandler {
    private final MessageTranslator messageTranslator;

    @Autowired
    public WebExceptionHandler(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    /**
     * Handles {@link InvalidQueryStringException}
     *
     * @param e exception
     * @return response entity containing {@link com.epam.esm.exception.ErrorResponse} and http status
     */
    @ExceptionHandler(InvalidQueryStringException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidQueryStringException(
            InvalidQueryStringException e) {
        String messageKey = e.getMessageKey();
        Object[] messageArgs = {e.getQueryPart()};
        ErrorResponse response =
                handleErrorResponseWithLog(messageKey, messageArgs, INVALID_QUERY_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link BindException}
     *
     * @param e exception
     * @return response entity containing {@link com.epam.esm.exception.ErrorResponse} and http status
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        Object[] args =
                (fieldError == null)
                        ? null
                        : new Object[]{fieldError.getField() + "=" + fieldError.getRejectedValue()};
        ErrorResponse response = handleErrorResponse(ARGUMENT_NOT_VALID, args, ARGUMENT_NOT_VALID_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link DuplicateKeyException}
     *
     * @param e exception
     * @return response entity containing {@link com.epam.esm.exception.ErrorResponse} and http status
     */
    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        ErrorResponse response = handleErrorResponse(DUPLICATED_ENTITY, null, DUPLICATED_ENTITY_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link InvalidFormatException}
     *
     * @param e exception
     * @return response entity containing {@link ErrorResponse} and http status
     */
    @ExceptionHandler(InvalidFormatException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException e) {
        log.warn(e);
        Object[] messageArgs = new Object[]{e.getValue()};
        ErrorResponse response =
                handleErrorResponse(FORMAT_NOT_VALID, messageArgs, FORMAT_NOT_VALID_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link MismatchedInputException}
     *
     * @param e exception
     * @return response entity containing {@link ErrorResponse} and http status
     */
    @ExceptionHandler(MismatchedInputException.class)
    protected ResponseEntity<ErrorResponse> handleMismatchedInputException(
            MismatchedInputException e) {
        log.warn(e);
        Object[] messageArgs = new Object[]{e};
        ErrorResponse response =
                handleErrorResponse(MISMATCH_INPUT, messageArgs, FORMAT_NOT_VALID_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link EntityNotPresentException}
     *
     * @param e exception
     * @return response entity containing {@link ErrorResponse} and http status
     */
    @ExceptionHandler(EntityNotPresentException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotPresentException(
            EntityNotPresentException e) {
        String messageKey = e.getMessageKey();
        Object[] messageArgs = {e.getEntityId()};
        ErrorResponse response =
                handleErrorResponseWithLog(messageKey, messageArgs, ENTITY_NOT_PRESENT_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    /**
     * Handles {@link EntityNotValidException}
     *
     * @param e exception
     * @return response entity containing {@link ErrorResponse} and http status
     */
    @ExceptionHandler(EntityNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotValidException(EntityNotValidException e) {
        String messageKey = e.getMessageKey();
        ErrorResponse response = handleErrorResponseWithLog(messageKey, null, ENTITY_NOT_VALID_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link MissingServletRequestParameterException}
     *
     * @param e exception
     * @return response entity containing {@link com.epam.esm.exception.ErrorResponse} and http status
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        ErrorResponse response =
                handleErrorResponseWithLog(
                        ARGUMENT_NOT_VALID,
                        new Object[]{e.getParameterName() + "=null"},
                        ARGUMENT_NOT_VALID_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link MethodArgumentTypeMismatchException}
     *
     * @param e exception
     * @return response entity containing {@link com.epam.esm.exception.ErrorResponse} and http status
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        ErrorResponse response =
                handleErrorResponseWithLog(
                        ARGUMENT_NOT_VALID,
                        new Object[]{e.getName() + "=" + e.getValue()},
                        ARGUMENT_NOT_VALID_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link ConstraintViolationException}
     *
     * @param e exception
     * @return response entity containing {@link com.epam.esm.exception.ErrorResponse} and http status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException e) {
        Optional<ConstraintViolation<?>> violationOptional =
                e.getConstraintViolations().stream().findFirst();
        ConstraintViolation<?> violation = violationOptional.orElseThrow(() -> new RuntimeException(e));
        String[] violationPaths = violation.getPropertyPath().toString().split("\\.");
        String violationValue =
                violationPaths.length > 0
                        ? violationPaths[violationPaths.length - 1]
                        : violation.getPropertyPath().toString();

        ErrorResponse response =
                handleErrorResponseWithLog(
                        violationValue + ": " + violation.getMessage(), null, ARGUMENT_NOT_VALID_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link NoHandlerFoundException}
     *
     * @param e exception
     * @return response entity containing {@link ErrorResponse} and http status
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleException(NoHandlerFoundException e) {
        ErrorResponse response = handleErrorResponse(HANDLER_NOT_FOUND, null, HANDLER_NOT_FOUND_CODE);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException e) {
        log.warn(e);
        ErrorResponse response = handleErrorResponse(INVALID_INTEGRITY, null, INVALID_INTEGRITY_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        String messageKey = e.getMessageKey();
        log.error(messageTranslator.getMessageForDefaultLocale(messageKey, null));
        ErrorResponse response = handleErrorResponse(messageKey, null, SERVICE_UNKNOWN_EXCEPTION_CODE);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserServiceException.class)
    protected ResponseEntity<ErrorResponse> handleUserServiceException(UserServiceException e) {

        ErrorResponse response = handleErrorResponse(e.getMessageKey(), new Object[]{e.getUserName()},
                ENTITY_NOT_VALID_CODE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse handleErrorResponseWithLog(
            String messageKey, Object[] messageArgs, int errorCode) {
        log.warn(messageTranslator.getMessageForDefaultLocale(messageKey, messageArgs));
        return handleErrorResponse(messageKey, messageArgs, errorCode);
    }

    private ErrorResponse handleErrorResponse(
            String messageKey, Object[] messageArgs, int errorCode) {
        String message = messageTranslator.getMessageForLocale(messageKey, messageArgs);
        return new ErrorResponse(message, errorCode);
    }
}
