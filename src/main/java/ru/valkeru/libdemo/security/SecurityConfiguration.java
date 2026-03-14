package ru.valkeru.libdemo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.valkeru.libdemo.config.security.TokenValidationFilter;
import ru.valkeru.libdemo.config.security.LibraryLogoutHandler;
import ru.valkeru.libdemo.service.core.security.UserService;
import ru.valkeru.libdemo.service.infrastructure.application.SecurityApplicationService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityApplicationService securityApplicationService;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) {
        security
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf(AbstractHttpConfigurer::disable) // NOSONAR
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority(Role.ROLE_ADMIN.name())
                        .requestMatchers("/service/**").hasAnyAuthority(Role.ROLE_LIBRARIAN.name(), Role.ROLE_ADMIN.name())
                        .requestMatchers("/personal/**").hasAuthority(Role.ROLE_USER.name())
                        .requestMatchers("/security/revoke-sessions").authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userService)
                .logout(logout -> logout
                        .logoutUrl("/security/sign-out")
                        .addLogoutHandler(new LibraryLogoutHandler(securityApplicationService))
                        .logoutSuccessHandler((rq, rs, auth) -> SecurityContextHolder.clearContext()))
                .addFilterBefore(new TokenValidationFilter(securityApplicationService), BasicAuthenticationFilter.class);

        return security.build();
    }
}
