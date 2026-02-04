package ru.valkeru.libdemo.component.adapter.request;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import ru.valkeru.libdemo.component.logger.HttpDebugLogger;
import ru.valkeru.libdemo.constants.Profiles;

import java.lang.reflect.Type;

@ControllerAdvice
@RequiredArgsConstructor
@Profile({Profiles.PROFILE_DEV, Profiles.PROFILE_PRE_PRODUCTION, Profiles.PROFILE_TEST})
public class RequestBodyLoggingAdapter extends RequestBodyAdviceAdapter {

    private final HttpServletRequest request;
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

        JsonView viewAnnotation = parameter.getParameterAnnotation(JsonView.class);

        Class<?> deserializationView = viewAnnotation != null
                ? viewAnnotation.value()[0]
                : null;

        debugLogger.logRequestBody(body, request, deserializationView);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
