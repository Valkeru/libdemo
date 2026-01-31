package ru.valkeru.libdemo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.valkeru.libdemo.config.security.TokenValidationFilter;
import ru.valkeru.libdemo.config.security.LibraryLogoutHandler;
import ru.valkeru.libdemo.service.core.SecurityService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityService securityService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable) // NOSONAR
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/service/**").hasAnyAuthority(Role.LIBRARIAN.name(), Role.ADMIN.name())
                        .requestMatchers("/personal/**").hasAuthority(Role.USER.name())
                        .requestMatchers("/security/revoke-sessions").authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(securityService)
                .logout(logout -> logout
                        .logoutUrl("/security/sign-out")
                        .addLogoutHandler(new LibraryLogoutHandler(securityService))
                        .logoutSuccessHandler((rq, rs, auth) -> SecurityContextHolder.clearContext()))
                .addFilterBefore(new TokenValidationFilter(securityService), BasicAuthenticationFilter.class);

        return security.build();
    }
}
