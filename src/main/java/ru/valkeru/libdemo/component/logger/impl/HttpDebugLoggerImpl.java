package ru.valkeru.libdemo.component.logger.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.component.logger.AbstractHttpDebugLogger;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class HttpDebugLoggerImpl extends AbstractHttpDebugLogger {

    public HttpDebugLoggerImpl(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public void logRequestParameters(HttpServletRequest request) {
        StringBuilder logMessageBuilder = new StringBuilder();

        try {
            buildMessageWithParameters(request, logMessageBuilder);

            writeRequestLog(logMessageBuilder.toString());
        } catch (JacksonException e) {
            writeLogFailed(e);
        }
    }

    @Override
    public void logRequestBody(Object requestBody, HttpServletRequest request) {
        StringBuilder logMessageBuilder = new StringBuilder();

        try {
            buildMessageWithRequestBody(requestBody, request, logMessageBuilder);

            writeRequestLog(logMessageBuilder.toString());
        } catch (JacksonException e) {
            writeLogFailed(e);
        }
    }

    @Override
    public void logResponseBody(Object body, HttpServletRequest request, HttpServletResponse response) {
        StringBuilder logMessageBuilder = new StringBuilder();

        try {
            buildMessageWithResponseBody(body, request, response, logMessageBuilder);

            writeResponseLog(logMessageBuilder.toString());
        } catch (JacksonException e) {
            writeLogFailed(e);
        }
    }
}
