package ru.valkeru.libdemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("!no-security")
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
//        security
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(
//                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
//                                .requestMatchers("/**")
//                                .permitAll()
//                );

        return security.build();
    }
}
