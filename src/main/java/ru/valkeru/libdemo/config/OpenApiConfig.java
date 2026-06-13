package ru.valkeru.libdemo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
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
    public static final String RESOURCE_ID_HEADER_REF = "#/components/headers/ResourceIdHeader";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
            .components(new Components()
                .addHeaders("RequestIdHeader", new Header()
                    .description("Unique request ID")
                    .schema(new UUIDSchema().example("7066f4ef-9c6e-4086-af07-d072975e37d7"))
                )
                .addHeaders("ResourceIdHeader", new Header()
                    .description("Created resource ID")
                    .schema(new UUIDSchema().example("539489eb-726c-4981-8791-ed488d81584d"))
                )
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
