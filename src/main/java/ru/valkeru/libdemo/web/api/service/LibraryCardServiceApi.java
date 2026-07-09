package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;

import java.util.UUID;

public interface LibraryCardServiceApi {

    @Operation(
        summary = "Add a library card",
        tags = {ApiTags.SERVICE, ApiTags.LIBRARY_CARD},
        responses = {
            @ApiResponse(responseCode = "201", description = "Created", headers = {
                @Header(name = HttpHeaders.LOCATION)
            }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            ),
            @ApiResponse(responseCode = "409", description = "Data integrity violation", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            )
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).SERVICE_LIBRARY_CARD_CREATE.name())")
    @PostMapping("/service/library-card")
    ResponseEntity<Void> createLibraryCard(@RequestBody LibraryCardCreateDto dto);

    @Operation(
        summary = "Get a library card",
        tags = { ApiTags.SERVICE, ApiTags.LIBRARY_CARD },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                @Content(schema = @Schema(implementation = ErrorDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {
                @Content(schema = @Schema(implementation = ErrorDto.class))
            })
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).SERVICE_LIBRARY_CARD_VIEW.name())")
    @GetMapping("/service/library-card/{id}")
    ResponseEntity<Object> getLibraryCard(@PathVariable UUID id);
}
