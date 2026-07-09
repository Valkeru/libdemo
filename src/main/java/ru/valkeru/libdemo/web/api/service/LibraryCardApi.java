package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

public interface LibraryCardApi {

    @Operation(
        summary = "Create a library card",
        description = """
            Create own library card. Response may include replacement for authentication tokens.
            If present, the client MUST replace the stored tokens
            """,
        tags = { ApiTags.LIBRARY_CARD },
        responses = {
            @ApiResponse(responseCode = "201", description = "Created", headers = {
                @Header(name = HttpHeaders.LOCATION, required = true),
                @Header(name = CustomHeaders.ACCESS_TOKEN, ref = OpenApiConfig.ACCESS_TOKEN_HEADER_REF),
                @Header(name = CustomHeaders.REFRESH_TOKEN, ref = OpenApiConfig.REFRESH_TOKEN_HEADER_REF)
            })
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PostMapping("/v1/library-card")
    ResponseEntity<Void> createLibraryCard(@Parameter(hidden = true) @AuthenticationPrincipal LibraryPrincipal principal);

    @Operation(
        summary = "Get current library card",
        tags = { ApiTags.LIBRARY_CARD },
        responses = {

        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("""
        hasAnyRole(
            T(ru.valkeru.libdemo.security.Role).ROLE_USER.getRoleName(),
            T(ru.valkeru.libdemo.security.Role).ROLE_READER.getRoleName()
        )
    """)
    @GetMapping("/v1/library-card")
    ResponseEntity<Object> getCurrentLibraryCard(@Parameter(hidden = true) @AuthenticationPrincipal LibraryPrincipal principal);

    @Operation(
        summary = "Get a library card",
        tags = { ApiTags.LIBRARY_CARD },
        responses = {

        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @GetMapping("/v1/library-card/{id}")
    ResponseEntity<Object> getLibraryCard(@PathVariable UUID id,
                                          @Parameter(hidden = true) @AuthenticationPrincipal LibraryPrincipal principal);
}
