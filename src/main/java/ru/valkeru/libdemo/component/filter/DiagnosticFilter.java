package ru.valkeru.libdemo.component.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.util.RequestExecutionContext;
import ru.valkeru.libdemo.util.RequestIdUtil;

import java.io.IOException;

@Component
@Order(Integer.MIN_VALUE)
@RequiredArgsConstructor
public class DiagnosticFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            RequestIdUtil.setMDCRequestId();

            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 0);
            RequestExecutionContext.store(RequestExecutionContext.REQUEST_WRAPPER_KEY, requestWrapper);

            addDiagnosticHeaders(response);

            filterChain.doFilter(requestWrapper, response);
        } finally {
            RequestIdUtil.clearMDCRequestId();
            RequestExecutionContext.clear();
        }
    }

    private void addDiagnosticHeaders(HttpServletResponse response) {
        response.setHeader(ApiConfig.REQUEST_ID_HEADER_NAME, RequestIdUtil.getMDCRequestId().toString());
    }
}
