package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@RequestMapping(SeriesServiceApi.SERIES_SERVICE_PATH)
public interface SeriesServiceApi {

    String SERIES_SERVICE_PATH = "/service/series";

    @Operation(
        summary = "Create a series",
        tags = {ApiTags.SERIES, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "201", ref = ApiConfig.REF_RESPONSE_RESOURCE_CREATED),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        },
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PostMapping
    ResponseEntity<Void> createSeries(@Valid @RequestBody SeriesEditDto series);

    @Operation(
        summary = "Update a series",
        tags = {ApiTags.SERIES, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        },
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PatchMapping("/{id}")
    ResponseEntity<Void> updateSeries(@PathVariable @Schema(description = "Series ID") UUID id,
                                      @RequestBody @Valid SeriesEditDto series);

    @Operation(
        summary = "Delete a series",
        tags = {ApiTags.SERIES, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "409",ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        },
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSeries(@PathVariable @Schema(description = "Series ID") UUID id);
}
