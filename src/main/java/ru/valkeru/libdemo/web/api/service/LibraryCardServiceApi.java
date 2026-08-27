package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;

import java.util.UUID;

@SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(LibraryCardServiceApi.LIBRARY_CARD_SERVICE_PATH)
public interface LibraryCardServiceApi {

    String LIBRARY_CARD_SERVICE_PATH = "/service/library-card";

    @Operation(
        summary = "Add a library card — staff operation",
        tags = {ApiTags.SERVICE, ApiTags.LIBRARY_CARD},
        responses = {
            @ApiResponse(responseCode = "201", ref = ApiConfig.REF_RESPONSE_RESOURCE_CREATED),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).SERVICE_LIBRARY_CARD_CREATE.name())")
    @PostMapping
    ResponseEntity<Void> createLibraryCardStaff(@RequestBody LibraryCardCreateDto dto);

    @Operation(
        summary = "Get a library card",
        tags = { ApiTags.SERVICE, ApiTags.LIBRARY_CARD},
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).SERVICE_LIBRARY_CARD_VIEW.name())")
    @GetMapping("/{id}")
    ResponseEntity<Object> getLibraryCard(@PathVariable UUID id);
}
