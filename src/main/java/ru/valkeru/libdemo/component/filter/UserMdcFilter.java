package ru.valkeru.libdemo.component.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.io.IOException;

@Order(SecurityFilterProperties.BASIC_AUTH_ORDER + 1)
@Component
public class UserMdcFilter extends OncePerRequestFilter {

    private static final String MDC_KEY_USER_ID = "userId";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LibraryPrincipal principal) {
            MDC.put(MDC_KEY_USER_ID, principal.id().toString());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY_USER_ID);
        }
    }
}
