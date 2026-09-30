package ru.valkeru.libdemo.config.api;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.jspecify.annotations.Nullable;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.dto.error.FormFieldErrorDto;

@Configuration
@SecurityScheme(
    name = ApiConfig.ACCESS_TOKEN_SCHEME,
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@OpenAPIDefinition(
    tags = {
        @Tag(name = ApiTags.AUTHOR, description = "Authors"),
        @Tag(name = ApiTags.CYCLE, description = "Cycles"),
        @Tag(name = ApiTags.SERIES, description = "Series"),
        @Tag(name = ApiTags.BOOK, description = "Books"),
        @Tag(name = ApiTags.LIBRARY_CARD, description = "Library card"),
        @Tag(name = ApiTags.SERVICE, description = "Service"),
        @Tag(name = ApiTags.SECURITY, description = "Security"),
    }
)
public class ApiConfig {

    public static final String ACCESS_TOKEN_SCHEME = "AccessToken";
    public static final String ACCESS_TOKEN_HEADER_NAME = "Access-Token";
    public static final String REFRESH_TOKEN_HEADER_NAME = "Refresh-Token";
    public static final String REQUEST_ID_HEADER_NAME = "Request-ID";


    public static final String REQUEST_ID_HEADER_REF = "#/components/headers/RequestIdHeader";
    public static final String ACCESS_TOKEN_HEADER_REF = "#/components/headers/AccessTokenHeader";
    public static final String REFRESH_TOKEN_HEADER_REF = "#/components/headers/RefreshTokenHeader";
    public static final String RESOURCE_LOCATION_HEADER_REF = "#/components/headers/CreatedResourceLocation";

    public static final String REF_RESPONSE_RESOURCE_CREATED = "#/components/responses/Created";
    public static final String REF_VALIDATION_ERROR_RESPONSE = "#/components/responses/ValidationError";
    public static final String REF_UNAUTHORIZED_RESPONSE = "#/components/responses/Unauthorized";
    public static final String REF_ACCESS_DENIED_RESPONSE = "#/components/responses/AccessDenied";
    public static final String REF_NOT_FOUND_RESPONSE = "#/components/responses/NotFound";
    public static final String REF_INTEGRITY_VIOLATION_RESPONSE = "#/components/responses/IntegrityViolation";

    public static final String SIGN_OUT_PATH = "/security/sign-out";

    /**
     * Basic OpenAPI docs parameters
     */
    @Bean
    public OpenAPI openApi() {
        Components components = new Components();
        addResponses(components);
        addHeaders(components);
        addSchemas(components);

        return new OpenAPI()
            .path(SIGN_OUT_PATH, getSignOutEndpoint())
            .components(components);
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return ApiConfig::addRequestIdHeadersToResponses;
    }

    /**
     * Adds responses components into API specs to use them as references in controller API responses descriptions
     */
    private static void addResponses(Components components) {
        components
            // 201
            .addResponses("Created", response("Successfully created", null)
                .addHeaderObject(HttpHeaders.LOCATION, new Header().$ref(RESOURCE_LOCATION_HEADER_REF)))
            // 400
            .addResponses("ValidationError", response("Validation error", arrayFormErrorSchemaRef()))
            // 401
            .addResponses("Unauthorized", response("Authentication required", null))
            // 403
            .addResponses("AccessDenied", response("Access denied", schemaRef(ErrorDto.class)))
            // 404
            .addResponses("NotFound", response("Not found", schemaRef(ErrorDto.class)))
            // 409
            .addResponses("IntegrityViolation", response("Data integrity violation", schemaRef(ErrorDto.class)));
    }

    /**
     * Adds headers components into API specs to use them as references in controller API responses descriptions
     */
    private static void addHeaders(Components components) {
        components
            .addHeaders("RequestIdHeader", new Header()
                .description("Unique request ID")
                .schema(new UUIDSchema().example("7066f4ef-9c6e-4086-af07-d072975e37d7"))
            )

            .addHeaders("AccessTokenHeader", new Header()
                .description("Access token. If present, the client MUST replace the currently stored access token")
                .schema(new StringSchema().example("eyJhbGciOiJub25lIn0.eyJzdWIiOiIxMjM0NTY3...")))

            .addHeaders("RefreshTokenHeader", new Header()
                .description("Refresh token. If present, the client MUST replace the currently stored refresh token")
                .schema(new StringSchema().example("Fr4JKlidS26inAei6b1YADQns169i3V9")))

            .addHeaders("CreatedResourceLocation", new Header()
                .description("Created resource location")
                .schema(new StringSchema().example("https://example.com/v1/author/ec83c27d-783a-4ac6-93a5-b68b9ba31736")));
    }

    /**
     * Adds schemas components into API specs to use them as references in controller API responses descriptions
     */
    private static void addSchemas(Components components) {
        components
            .addSchemas("FormFieldErrorDto", generateSchema(FormFieldErrorDto.class));
    }

    /**
     * Creates sign out endpoint description to display it in API specs
     */
    private static PathItem getSignOutEndpoint() {
        PathItem pathItem = new PathItem();
        Operation operation = new Operation()
            .summary("Sign out")
            .operationId("signOut")
            .addTagsItem(ApiTags.SECURITY)
            .addSecurityItem(new SecurityRequirement().addList(ACCESS_TOKEN_SCHEME))
            .responses(new ApiResponses().addApiResponse("200", new ApiResponse().description("Signed out")));

        return pathItem.post(operation);
    }

    /**
     * Adds {@value ApiConfig#REQUEST_ID_HEADER_NAME} header to every non-ref response defined in API specs
     */
    private static void addRequestIdHeadersToResponses(OpenAPI openApi) {
        openApi.getPaths().values().stream()
            .flatMap(pathItem -> pathItem.readOperations().stream())
            .flatMap(operation -> operation.getResponses().values().stream())
            .filter(response -> response.get$ref() == null) // Skip $ref responses to follow OpenAPI specs
            .forEach(response -> response.addHeaderObject(REQUEST_ID_HEADER_NAME, getRequestIdHeaderRef()));
    }

    /**
     * Creates API response definition
     * @param description Description for response
     * @param schema Response schema. Pass {@code null} for empty response
     */
    private static ApiResponse response(String description, @Nullable Schema<?> schema) {
        ApiResponse response = new ApiResponse()
            .description(description)
            .addHeaderObject(REQUEST_ID_HEADER_NAME, getRequestIdHeaderRef());

        if (schema != null) {
            response
                .content(new Content().addMediaType(
                    MediaType.APPLICATION_JSON_VALUE,
                    new io.swagger.v3.oas.models.media.MediaType()
                        .schema(schema)
                ));
        }

        return response;
    }

    /**
     * Returns {@link ApiConfig#REQUEST_ID_HEADER_NAME} header reference
     */
    private static Header getRequestIdHeaderRef() {
        return new Header().$ref(REQUEST_ID_HEADER_REF);
    }

    /**
     * Returns schema reference for passed class name
     */
    private static Schema<?> schemaRef(Class<?> model) {
        return new Schema<>().$ref(model.getSimpleName());
    }

    /**
     * Returns array schema for FormFieldErrorDto
     */
    private static ArraySchema arrayFormErrorSchemaRef() {
        return new ArraySchema().items(schemaRef(FormFieldErrorDto.class));
    }

    /**
     * Creates schema for passed model class
     */
    @SuppressWarnings("all")
    private static Schema<?> generateSchema(Class<?> model) {
        return ModelConverters.getInstance().read(model).get(model.getSimpleName());
    }
}
