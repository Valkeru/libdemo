package ru.valkeru.libdemo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.valkeru.libdemo.constants.CustomHeaders;

@Configuration
@SecurityScheme(
    name = OpenApiConfig.ACCESS_TOKEN_SCHEME,
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = OpenApiConfig.ACCESS_TOKEN_HEADER_NAME,
    description = "API access token"
)
@SuppressWarnings("java:S1118")
public class OpenApiConfig {

    public static final String ACCESS_TOKEN_SCHEME = "AccessToken";
    public static final String ACCESS_TOKEN_HEADER_NAME = "Access-Token";
    public static final String REQUEST_ID_HEADER_REF = "#/components/headers/RequestIdHeader";
    public static final String ACCESS_TOKEN_HEADER_REF = "#/components/headers/AccessTokenHeader";
    public static final String REFRESH_TOKEN_HEADER_REF = "#/components/headers/RefreshTokenHeader";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
            .components(new Components()
                .addHeaders("RequestIdHeader", new Header()
                    .description("Unique request ID")
                    .schema(new UUIDSchema().example("7066f4ef-9c6e-4086-af07-d072975e37d7"))
                )
                .addHeaders("AccessTokenHeader", new Header()
                    .description("Access token replacement. If present, the client MUST replace the stored access token")
                    .schema(new StringSchema().example("eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJlNGFmNmQ5YS1hYWM0LTQ1MzgtOWQzNC1mNDc4YTY0YmUzOTQiLCJzdWIiOiJhZG1pbiIsImlhdCI6MTc4MzQwNzU2MSwiZXhwIjoxNzgzNDExMTYxLCJ1c2VySWQiOiJlOGI4M2FhYy01YmJlLTQwZTItYjg1Yi1kZDE0MzdmZTMwMjIiLCJyb2xlIjoiUk9MRV9BRE1JTiJ9.MAvPjTzooL-JY2fkUKpBK9M7FkcuMRiIoUugBJd8u5P2RKaEkKFVNXFURkeiW9VxAafcZAuArrHGI9DtMYq9Tg")))
                .addHeaders("RefreshTokenHeader", new Header()
                    .description("Refresh token replacement. If present, the client MUST replace the stored refresh token")
                    .schema(new StringSchema().example("Fr4JKlidS26inAei6b1YADQns169i3V9")))
            );
    }

    @Bean
    public OpenApiCustomizer requestIdHeaderCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
            pathItem.readOperations().forEach(operation ->
                operation.getResponses().forEach(((s, apiResponse) ->
                        apiResponse.addHeaderObject(CustomHeaders.REQUEST_ID, new Header().$ref(REQUEST_ID_HEADER_REF))
                    )
                )
            )
        );
    }
}
