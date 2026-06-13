package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
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
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.web.api.LibraryCommonApi;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(SeriesServiceApi.SERIES_SERVICE_URL)
public interface SeriesServiceApi extends LibraryCommonApi {

    String SERIES_SERVICE_URL = "/service/series";

    @Operation(
        summary = "Create a series",
        tags = {ApiTags.SERIES, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Series created",
                headers = @Header(name = CustomHeaders.RESOURCE_ID, ref = OpenApiConfig.RESOURCE_ID_HEADER_REF)
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
        }
    )
    @PostMapping
    default ResponseEntity<Void> createSeries(@RequestBody @Valid SeriesDto series) {
        return defaultApiResponse();
    }

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
        }
    )
    @PatchMapping("/{seriesId}")
    default ResponseEntity<SeriesDto> updateSeries(@PathVariable(name = "seriesId")
                                                   @Schema(description = "Series ID") UUID id,
                                                   @RequestBody @Valid SeriesDto series) {
        return defaultApiResponse();
    }

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
        }
    )
    @DeleteMapping("/{seriesId}")
    default ResponseEntity<Void> deleteSeries(@PathVariable(name = "seriesId")
                                              @Schema(description = "Series ID") UUID id) {
        return defaultApiResponse();
    }
}
