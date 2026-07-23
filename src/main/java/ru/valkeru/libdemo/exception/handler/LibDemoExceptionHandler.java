package ru.valkeru.libdemo.exception.handler;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.valkeru.libdemo.component.message.MessageProvider;
import ru.valkeru.libdemo.exception.BadRequestException;
import ru.valkeru.libdemo.exception.IntegrityViolationException;
import ru.valkeru.libdemo.exception.NotFoundException;
import ru.valkeru.libdemo.exception.impl.ReadersCardRestrictedException;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.dto.error.FormFieldErrorDto;
import ru.valkeru.libdemo.persistence.exception.ConflictPersistenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class LibDemoExceptionHandler {

    private final MessageProvider messageProvider;

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto handleException(Exception e) {
        log.error("Internal error: {}", e.getMessage(), e);

        return buildErrorDto(messageProvider.getInternalErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Hidden
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handleResponseStatusException(ResponseStatusException rse) {
        ErrorDto errorDto = buildErrorDto(rse.getReason(), (HttpStatus) rse.getStatusCode());

        return ResponseEntity.status(errorDto.status()).body(errorDto);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<FormFieldErrorDto> validationExceptionHandler(MethodArgumentNotValidException manve) {
        log.warn("Request validation failed: {}", getRootMessage(manve), manve);

        Map<String, List<String>> fieldErrorsMap = manve.getFieldErrors().stream()
            .collect(Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.mapping(
                        FieldError::getDefaultMessage,
                        Collectors.toList()
                    )
                )
            );

        List<FormFieldErrorDto> violations = new ArrayList<>();

        for (Map.Entry<String, List<String>> fieldErrors : fieldErrorsMap.entrySet()) {
            FormFieldErrorDto violation = new FormFieldErrorDto(fieldErrors.getKey(), fieldErrors.getValue());
            violations.add(violation);
        }

        return violations;
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleEntityNotFound(NotFoundException nfe) {
        log.info("Entity not found: {}", nfe.getMessage(), nfe);

        return buildErrorDto(nfe, HttpStatus.NOT_FOUND);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequest(BadRequestException bre) {
        log.info("Invalid request (validation), reason: {}", bre.getMessage(), bre);

        return buildErrorDto(bre, HttpStatus.BAD_REQUEST);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorDto handleIntegrityViolations(DataIntegrityViolationException dive) {
        log.info("Data violation: {}", getRootMessage(dive), dive);

        return buildErrorDto(messageProvider.getDataIntegrityMessage(dive), HttpStatus.CONFLICT);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IntegrityViolationException.class)
    public ErrorDto handleIntegrityViolations(IntegrityViolationException ive) {
        log.info("Data integrity violation: {}", ive.getMessage(), ive);

        return buildErrorDto(ive, HttpStatus.CONFLICT);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PropertyReferenceException.class)
    public ErrorDto handlePropertyReference(PropertyReferenceException pre) {
        log.warn("Property reference exception: {}", pre.getMessage());

        return buildErrorDto("Invalid sort field: %s".formatted(pre.getPropertyName()), HttpStatus.BAD_REQUEST);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorDto handleUserNotFound(AuthenticationException ae) {
        log.info("Authentication failed");
        log.info(getRootMessage(ae));

        return buildErrorDto("Invalid login or password", HttpStatus.UNAUTHORIZED);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ErrorDto handleAuthorizationDeniedException() {
        return buildErrorDto("Access denied", HttpStatus.FORBIDDEN);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ReadersCardRestrictedException.class)
    public ErrorDto handle(ReadersCardRestrictedException lcre) {
        return buildErrorDto(lcre, HttpStatus.CONFLICT);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorDto handle(HttpRequestMethodNotSupportedException mnse) {
        String[] supportedMethods = mnse.getSupportedMethods();
        String joinedMethods = StringUtils.join(supportedMethods, ", ");

        String message = "Method %s is not supported here! Supported methods are: %s"
            .formatted(mnse.getMethod(), joinedMethods);

        return buildErrorDto(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictPersistenceException.class)
    public ErrorDto handleConflict(ConflictPersistenceException cpe) {
        return buildErrorDto(cpe, HttpStatus.CONFLICT);
    }

    private ErrorDto buildErrorDto(Exception e, HttpStatus status) {
        return buildErrorDto(e.getMessage(), status);
    }

    private ErrorDto buildErrorDto(String message, HttpStatus status) {
        return new ErrorDto(status, message);
    }

    private String getRootMessage(Exception e) {
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        String message = rootCause.getMessage();

        return StringUtils.isNotBlank(message) ? message : e.getMessage();
    }
}
