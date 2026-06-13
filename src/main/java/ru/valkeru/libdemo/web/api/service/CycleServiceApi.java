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
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.web.api.LibraryCommonApi;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(CycleServiceApi.CYCLE_SERVICE_URL)
public interface CycleServiceApi extends LibraryCommonApi {

    String CYCLE_SERVICE_URL = "/service/cycle";

    @Operation(
        summary = "Add a cycle",
        tags = {ApiTags.CYCLE, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Created",
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
    default ResponseEntity<Void> addCycle(@RequestBody @Valid CycleDto cycleDto) {
        return defaultApiResponse();
    }

    @Operation(
        summary = "Update a cycle data",
        tags = {ApiTags.CYCLE, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Updated"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Data is not exists",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
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
    @PatchMapping("/{cycleId}")
    default ResponseEntity<Void> updateCycle(@PathVariable(name = "cycleId")
                                             @Schema(description = "ID цикла") UUID id,
                                             @RequestBody CycleDto cycleDto) {
        return defaultApiResponse();
    }

    @Operation(
        summary = "Remove a cycle",
        tags = {ApiTags.CYCLE, ApiTags.SERVICE},
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
    @DeleteMapping("/{cycleId}")
    default ResponseEntity<Void> deleteCycle(@PathVariable(name = "cycleId")
                                             @Schema(description = "ID цикла") UUID id) {
        return defaultApiResponse();
    }
}
