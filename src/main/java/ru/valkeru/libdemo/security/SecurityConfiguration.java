package ru.valkeru.libdemo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.config.security.TokenValidationFilter;
import ru.valkeru.libdemo.config.security.LibraryLogoutHandler;
import ru.valkeru.libdemo.infrastructure.security.UserService;
import ru.valkeru.libdemo.application.SecurityApplicationService;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityApplicationService securityApplicationService;
    private final UserService userService;
    private final ObjectMapper mapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) {
        security
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());

                    ErrorDto dto = new ErrorDto(HttpStatus.FORBIDDEN, "Access denied");
                    mapper.writeValue(response.getOutputStream(), dto);
                }))
            )
            .csrf(AbstractHttpConfigurer::disable) // NOSONAR
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/service/**").hasAnyRole(Role.getServiceRoleNames())
                .requestMatchers("/security/revoke-sessions").authenticated()
                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .anyRequest().permitAll())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .userDetailsService(userService)
            .logout(logout -> logout
                .logoutUrl(ApiConfig.SIGN_OUT_PATH)
                .addLogoutHandler(new LibraryLogoutHandler(securityApplicationService))
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
            .addFilterBefore(new TokenValidationFilter(securityApplicationService), BasicAuthenticationFilter.class);

        return security.build();
    }
}
