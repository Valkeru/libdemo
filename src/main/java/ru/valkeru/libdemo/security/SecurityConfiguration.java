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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityApplicationService securityApplicationService;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) {
        security
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .csrf(AbstractHttpConfigurer::disable) // NOSONAR
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/service/**").hasAnyRole(Role.getServiceRoleNames())
                .requestMatchers("/security/revoke-sessions").authenticated()
                .requestMatchers("/admin/**").hasRole(Role.ROLE_ADMIN.getRoleName())
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
