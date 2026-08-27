package ru.valkeru.libdemo.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.model.dto.LibraryCardDto;
import ru.valkeru.libdemo.model.dto.LibraryCardListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

@RequestMapping(LibraryCardApi.LIBRARY_CARD_V1_PATH)
@SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
public interface LibraryCardApi {

    String LIBRARY_CARD_V1_PATH = "/v1/library-card";

    @Operation(
        summary = "Create a library card — user operation",
        description = """
            Create own library card. Response may include replacement for authentication tokens.
            If present, the client MUST replace the stored tokens
            """,
        tags = {ApiTags.LIBRARY_CARD},
        responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created", headers = {
                @Header(name = HttpHeaders.LOCATION, ref = ApiConfig.RESOURCE_LOCATION_HEADER_REF, required = true),
                @Header(name = ApiConfig.ACCESS_TOKEN_HEADER_NAME, ref = ApiConfig.ACCESS_TOKEN_HEADER_REF),
                @Header(name = ApiConfig.REFRESH_TOKEN_HEADER_NAME, ref = ApiConfig.REFRESH_TOKEN_HEADER_REF)
            }),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE)
        }
    )
    @PreAuthorize("""
            hasAnyRole(
                T(ru.valkeru.libdemo.security.Role).USER,
                T(ru.valkeru.libdemo.security.Role).READER
            )
        """)
    @PostMapping
    ResponseEntity<Void> createLibraryCardUser(@Parameter(hidden = true) @AuthenticationPrincipal LibraryPrincipal principal);

    @Operation(
        summary = "Get all library cards",
        tags = {ApiTags.LIBRARY_CARD},
        responses = {
            @ApiResponse(responseCode = "200", description = "Library cards list"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE)
        },
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("""
            hasAnyRole(
                T(ru.valkeru.libdemo.security.Role).USER,
                T(ru.valkeru.libdemo.security.Role).READER
            )
        """)
    @GetMapping("/all")
    ResponseEntity<Page<LibraryCardListDto>> getLibraryCards(@Parameter(hidden = true)
                                                             @AuthenticationPrincipal LibraryPrincipal principal,
                                                             @ParameterObject @PageableDefault Pageable pageable);

    @Operation(
        summary = "Get current library card",
        tags = {ApiTags.LIBRARY_CARD},
        responses = {
            @ApiResponse(responseCode = "200", description = "Current library card"),
            @ApiResponse(responseCode = "204", description = "There is no current library card", content = @Content),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE)
        },
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("""
            hasAnyRole(
                T(ru.valkeru.libdemo.security.Role).USER,
                T(ru.valkeru.libdemo.security.Role).READER
            )
        """)
    @GetMapping
    ResponseEntity<LibraryCardDto> getCurrentLibraryCard(@Parameter(hidden = true)
                                                         @AuthenticationPrincipal LibraryPrincipal principal);

    @Operation(
        summary = "Get a library card",
        tags = {ApiTags.LIBRARY_CARD},
        responses = {
            @ApiResponse(responseCode = "200", description = "Current library card"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        },
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("""
            hasAnyRole(
                T(ru.valkeru.libdemo.security.Role).USER,
                T(ru.valkeru.libdemo.security.Role).READER
            )
        """)
    @GetMapping("/{id}")
    ResponseEntity<LibraryCardDto> getLibraryCard(@PathVariable @Schema(description = "Card ID") UUID id,
                                                  @Parameter(hidden = true)
                                                  @AuthenticationPrincipal LibraryPrincipal principal);
}
