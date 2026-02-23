package ru.valkeru.libdemo.component.logger;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.AnnotationIntrospector;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.valkeru.libdemo.config.serialization.SecretIntrospector;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractHttpDebugLogger implements HttpDebugLogger {

    private static final String REQUEST_TEMPLATE = "REQUEST: method = [%s]; path = [%s], IP = [%s]";
    private static final String PARAMETERS_TEMPLATE = "; parameters = ";
    private static final String DESERIALIZED_TEMPLATE = "; deserialized value = ";
    private static final String RESPONSE_DATA = "; response data = ";
    private static final String SERIALIZED_TEMPLATE = "; serialized value = ";
    private static final char OPENING_BRACKET = '(';
    private static final char CLOSING_BRACKET = ')';

    private final ObjectMapper mapper;

    protected AbstractHttpDebugLogger(ObjectMapper mapper) {
        AnnotationIntrospector defaultIntrospector = mapper.serializationConfig().getAnnotationIntrospector();
        SecretIntrospector secretIntrospector = new SecretIntrospector();

        this.mapper = mapper.rebuild()
                .annotationIntrospector(AnnotationIntrospector.pair(secretIntrospector, defaultIntrospector))
                .build();
    }

    protected void buildMessageWithParameters(HttpServletRequest request, StringBuilder requestLogMessageBuilder) {
        Map<String, String> parameters = getParametersMap(request);

        formatRequestDataTemplate(request, requestLogMessageBuilder);

        if (!parameters.isEmpty()) {
            requestLogMessageBuilder
                    .append(PARAMETERS_TEMPLATE)
                    .append(OPENING_BRACKET)
                    .append(mapper.writeValueAsString(parameters))
                    .append(CLOSING_BRACKET);
        }
    }

    protected void buildMessageWithRequestBody(Object body, HttpServletRequest request,
                                               StringBuilder requestLogMessageBuilder) {
        formatRequestDataTemplate(request, requestLogMessageBuilder);

        requestLogMessageBuilder
                .append(DESERIALIZED_TEMPLATE)
                .append(OPENING_BRACKET)
                .append(writeBody(body))
                .append(CLOSING_BRACKET);
    }

    protected void buildMessageWithResponseBody(Object body, HttpServletRequest request, HttpServletResponse response,
                                                StringBuilder logMessageBuilder) {
        formatRequestDataTemplate(request, logMessageBuilder);

        logMessageBuilder
                .append(RESPONSE_DATA)
                .append(OPENING_BRACKET)
                .append(mapper.writeValueAsString(body))
                .append(CLOSING_BRACKET);

        List<Integer> successCodes = List.of(HttpStatus.OK.value(), HttpStatus.CREATED.value());
        if (successCodes.contains(response.getStatus())) {
            logMessageBuilder
                    .append(SERIALIZED_TEMPLATE)
                    .append(OPENING_BRACKET)
                    .append(writeBody(body))
                    .append(CLOSING_BRACKET);
        }
    }

    protected void writeRequestLog(String message) {
        log.debug("Request log: {}", message);
    }

    protected void writeResponseLog(String message) {
        log.debug("Response log: {}", message);
    }

    protected void writeLogFailed(JacksonException jpe) {
        log.debug("Log failed: {}", jpe.getMessage(), jpe);
    }

    private static void formatRequestDataTemplate(HttpServletRequest request, StringBuilder requestLogMessageBuilder) {
        requestLogMessageBuilder.append(
                String.format(REQUEST_TEMPLATE, request.getMethod(), request.getRequestURI(), request.getRemoteAddr())
        );
    }

    private Map<String, String> getParametersMap(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> parameters = new HashMap<>();

        while (parameterNames.hasMoreElements()) {
            String next = parameterNames.nextElement();
            parameters.put(next, request.getParameter(next));
        }

        return parameters;
    }

    private String writeBody(final Object body) {
        return mapper.writeValueAsString(body);
    }
}
