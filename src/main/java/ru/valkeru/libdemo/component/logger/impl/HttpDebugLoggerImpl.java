package ru.valkeru.libdemo.component.logger.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.component.logger.AbstractHttpDebugLogger;

@Component
public class HttpDebugLoggerImpl extends AbstractHttpDebugLogger {

    @Override
    public void logRequestParameters(HttpServletRequest request) {
        StringBuilder logMessageBuilder = new StringBuilder();

        buildMessageWithParameters(request, logMessageBuilder);

        writeRequestLog(logMessageBuilder.toString());
    }

    @Override
    public void logRequestBody(Object requestBody, HttpServletRequest request, final Class<?> deserializationView) {
        StringBuilder logMessageBuilder = new StringBuilder();

        buildMessageWithRequestBody(requestBody, request, logMessageBuilder, deserializationView);

        writeRequestLog(logMessageBuilder.toString());
    }

    @Override
    public void logResponseBody(Object body, HttpServletRequest request,
                                HttpServletResponse response, final Class<?> serializationView) {
        StringBuilder logMessageBuilder = new StringBuilder();

        buildMessageWithResponseBody(body, request, response, logMessageBuilder, serializationView);

        writeResponseLog(logMessageBuilder.toString());
    }
}
