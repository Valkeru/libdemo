package ru.valkeru.libdemo.component.adapter.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ru.valkeru.libdemo.component.logger.HttpDebugLogger;
import ru.valkeru.libdemo.constants.Profiles;
import ru.valkeru.libdemo.util.RequestExecutionContext;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
@Profile({Profiles.PROFILE_DEV, Profiles.PROFILE_PRE_PRODUCTION, Profiles.PROFILE_TEST})
public class LoggingAdapter extends RequestBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private final HttpDebugLogger debugLogger;

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type targetType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @NonNull
    @Override
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage,
                                @NonNull MethodParameter parameter, @NonNull Type targetType,
                                @NonNull Class<? extends HttpMessageConverter<?>> converterType) {

        RequestExecutionContext.store(RequestExecutionContext.READ_BODY, body);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public boolean supports(MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {

        Method method = returnType.getMethod();

        return Optional.ofNullable(method)
                .map(m -> m.getDeclaringClass().getPackage().getName().contains("ru.valkeru.libdemo"))
                .orElse(false);
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();
        if (status >= 400) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            Object deserializedRequestBody =
                RequestExecutionContext.getObject(RequestExecutionContext.READ_BODY, Object.class);

            debugLogger.logRequestParameters(servletRequest);
            debugLogger.logRequestBody(deserializedRequestBody, servletRequest);
            debugLogger.logResponseBody(body, servletRequest, servletResponse);
        }

        return body;
    }
}
