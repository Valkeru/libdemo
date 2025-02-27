package ru.valkeru.libdemo.exception.handler;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.valkeru.libdemo.component.message.MessageProvider;
import ru.valkeru.libdemo.exception.BadRequestException;
import ru.valkeru.libdemo.exception.IntegrityViolationException;
import ru.valkeru.libdemo.exception.NotFoundException;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.util.RequestExecutionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LibDemoExceptionHandler {

    MessageProvider messageProvider;

    @Hidden
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleEntityNotFound(NotFoundException nfe) {
        log.error("Entity not found: {}", nfe.getMessage(), nfe);

        return buildErrorDto(nfe, HttpStatus.NOT_FOUND);
    }

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequest(BadRequestException bre) {
        log.error("Invalid request (validation), reason: {}", bre.getMessage(), bre);

        return buildErrorDto(bre, HttpStatus.BAD_REQUEST);
    }

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorDto handleNotReadable(HttpMessageNotReadableException hmnre) {
        log.error("Invalid request, reason: {}; payload: {}",
                hmnre.getMessage(),
                RequestExecutionContext.readRequestBody(),
                hmnre
        );

        return buildErrorDto(messageProvider.getBadRequestMessage(hmnre), HttpStatus.BAD_REQUEST);
    }

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto validationExceptionHandler(MethodArgumentNotValidException manve) {
        log.error("Request validation failed: {}", manve.getMessage(), manve);

        Map<String, String> fieldErrorMap = new HashMap<>();
        manve.getFieldErrors().forEach(error -> fieldErrorMap.put(error.getField(), error.getDefaultMessage()));

        return buildErrorDto(fieldErrorMap, HttpStatus.BAD_REQUEST);
    }

    @Hidden
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorDto handleIntegrityViolations(DataIntegrityViolationException dive) {
        log.error("Data violation: {}", dive.getMessage(), dive);

        return buildErrorDto(messageProvider.getDataIntegrityMessage(dive), HttpStatus.CONFLICT);
    }

    @Hidden
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(IntegrityViolationException.class)
    public ErrorDto handleIntegrityViolations(IntegrityViolationException ive) {
        log.error("Data violation: {}", ive.getMessage(), ive);

        return buildErrorDto(ive, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ErrorDto handleException(Exception e) {
        log.error("Internal error: {}", e.getMessage(), e);

        return buildErrorDto(messageProvider.getInternalErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
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
