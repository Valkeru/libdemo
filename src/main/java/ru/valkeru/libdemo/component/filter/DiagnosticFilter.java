package ru.valkeru.libdemo.component.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.valkeru.libdemo.component.logger.HttpDebugLogger;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.util.RequestExecutionContext;
import ru.valkeru.libdemo.util.RequestIdUtil;

import java.io.IOException;

@Component
@Order(Integer.MIN_VALUE)
@RequiredArgsConstructor
public class DiagnosticFilter extends OncePerRequestFilter {

    private final HttpDebugLogger httpDebugLogger;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            RequestIdUtil.setMDCRequestId();
            putUserIdToMdc();

            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 0);
            RequestExecutionContext.store(RequestExecutionContext.REQUEST_WRAPPER_KEY, requestWrapper);

            addDiagnosticHeaders(response);
            httpDebugLogger.logRequestParameters(request);

            filterChain.doFilter(requestWrapper, response);
        } finally {
            // На случай, если заголовок потеряли
            if (!response.isCommitted()) {
                addDiagnosticHeaders(response);
            }

            RequestIdUtil.clearMDCRequestId();
            RequestExecutionContext.clear();
        }
    }

    private void addDiagnosticHeaders(HttpServletResponse response) {
        response.setHeader(CustomHeaders.REQUEST_ID, RequestIdUtil.getMDCRequestId().toString());
    }

    private void putUserIdToMdc() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }

        LibraryPrincipal principal = (LibraryPrincipal) authentication.getPrincipal();
        if (principal == null) {
            return;
        }

        MDC.put("userId", principal.id().toString());
    }
}
