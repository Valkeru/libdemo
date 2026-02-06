package ru.valkeru.libdemo.exception.handler;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import ru.valkeru.libdemo.model.dto.error.ErrorDto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class LibDemoExceptionHandler {

    private final MessageProvider messageProvider;

    @Hidden
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleEntityNotFound(NotFoundException nfe) {
        log.info("Entity not found: {}", nfe.getMessage(), nfe);

        return buildErrorDto(nfe, HttpStatus.NOT_FOUND);
    }

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequest(BadRequestException bre) {
        log.info("Invalid request (validation), reason: {}", bre.getMessage(), bre);

        return buildErrorDto(bre, HttpStatus.BAD_REQUEST);
    }

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto validationExceptionHandler(MethodArgumentNotValidException manve) {
        log.info("Request validation failed: {}", manve.getMessage(), manve);

        Map<String, String> fieldErrorMap = new HashMap<>();
        manve.getFieldErrors().forEach(error -> fieldErrorMap.put(error.getField(), error.getDefaultMessage()));

        return buildErrorDto(fieldErrorMap, HttpStatus.BAD_REQUEST);
    }

    @Hidden
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorDto handleIntegrityViolations(DataIntegrityViolationException dive) {
        log.info("Data violation: {}", dive.getMessage(), dive);

        return buildErrorDto(messageProvider.getDataIntegrityMessage(dive), HttpStatus.CONFLICT);
    }

    @Hidden
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(IntegrityViolationException.class)
    public ErrorDto handleIntegrityViolations(IntegrityViolationException ive) {
        log.info("Data integrity violation: {}", ive.getMessage(), ive);

        return buildErrorDto(ive, HttpStatus.CONFLICT);
    }

    @Hidden
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handleResponseStatusException(ResponseStatusException rse) {
        ErrorDto errorDto = buildErrorDto(rse.getReason(), (HttpStatus) rse.getStatusCode());

        return ResponseEntity.status(errorDto.status()).body(errorDto);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ErrorDto handleException(Exception e) {
        log.error("Internal error: {}", e.getMessage(), e);

        return buildErrorDto(messageProvider.getInternalErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public ErrorDto handleUserNotFound(AuthenticationException ae) {
        log.info("Authentication failed");
        log.info(ae.getMessage());

        return buildErrorDto("Неверное имя пользователя или пароль", HttpStatus.UNAUTHORIZED);
    }

    private ErrorDto buildErrorDto(Exception e, HttpStatus status) {
        return buildErrorDto(e.getMessage(), status);
    }

    private ErrorDto buildErrorDto(Map<String, String> errors, HttpStatus status) {
        String result = errors.entrySet().stream()
                .map(kv -> String.format("%s: %s", kv.getKey(), kv.getValue()))
                .collect(Collectors.joining("; "));

        return buildErrorDto(result, status);
    }

    private ErrorDto buildErrorDto(String message, HttpStatus status) {
        return new ErrorDto(status, message);
    }
}
