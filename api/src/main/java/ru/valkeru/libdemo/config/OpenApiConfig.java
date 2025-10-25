package ru.valkeru.libdemo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = OpenApiConfig.ACCESS_TOKEN_SCHEME,
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = OpenApiConfig.ACCESS_TOKEN_HEADER_NAME,
        description = "Токен доступа к API"
)
@SuppressWarnings("java:S1118")
public class OpenApiConfig {

    public static final String ACCESS_TOKEN_SCHEME = "AccessToken";
    public static final String ACCESS_TOKEN_HEADER_NAME = "Access-Token";
}
