package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

public interface SeriesServiceApi {

    String SERIES_SERVICE_URL = "/service/series";

    @Operation(
        summary = "Create a series",
        tags = {ApiTags.SERIES, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Series created",
                headers = @Header(name = HttpHeaders.LOCATION)
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Data integrity violation",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            )
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PostMapping("/service/series")
    ResponseEntity<Void> createSeries(@Valid @RequestBody SeriesEditDto series);

    @Operation(
        summary = "Update a series",
        tags = {ApiTags.SERIES, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Data is not exists",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Data integrity violation"
            )
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PatchMapping("/service/series/{id}")
    ResponseEntity<Void> updateSeries(@PathVariable @Schema(description = "Series ID") UUID id,
                                      @RequestBody @Valid SeriesEditDto series);

    @Operation(
        summary = "Delete a series",
        tags = {ApiTags.SERIES, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Success"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Data is not exists",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Data integrity violation",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            )
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @DeleteMapping("/service/series/{id}")
    ResponseEntity<Void> deleteSeries(@PathVariable @Schema(description = "Series ID") UUID id);
}
