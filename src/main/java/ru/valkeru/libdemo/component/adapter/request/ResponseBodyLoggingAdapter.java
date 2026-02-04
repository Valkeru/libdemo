package ru.valkeru.libdemo.component.adapter.request;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ru.valkeru.libdemo.component.logger.HttpDebugLogger;
import ru.valkeru.libdemo.constants.Profiles;

import java.lang.reflect.Method;
import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
@Profile({Profiles.PROFILE_DEV, Profiles.PROFILE_PRE_PRODUCTION, Profiles.PROFILE_TEST})
public class ResponseBodyLoggingAdapter implements ResponseBodyAdvice<Object> {

    private final HttpDebugLogger debugLogger;

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

        JsonView viewAnnotation = returnType.getMethodAnnotation(JsonView.class);
        Class<?> serializationView = Optional.ofNullable(viewAnnotation)
                .map(a -> a.value()[0])
                .orElse(null);

        debugLogger.logResponseBody(
                body, ((ServletServerHttpRequest) request).getServletRequest(),
                ((ServletServerHttpResponse) response).getServletResponse(), serializationView
        );

        return body;
    }
}
