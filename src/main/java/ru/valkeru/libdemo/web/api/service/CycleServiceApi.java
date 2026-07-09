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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.infrastructure.validation.groups.CycleGroups;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
public interface CycleServiceApi {

    String CYCLE_SERVICE_URL = "/service/cycle";

    @Operation(
        summary = "Add a cycle",
        tags = {ApiTags.CYCLE, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Created",
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
        }
    )
    @PostMapping("/service/cycle")
    ResponseEntity<Void> addCycle(@RequestBody @Validated(CycleGroups.CycleCreateGroup.class) CycleDto cycleDto);

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
    @PatchMapping("/service/cycle/{id}")
    ResponseEntity<Void> updateCycle(@PathVariable @Schema(description = "ID цикла") UUID id,
                                     @RequestBody @Validated(CycleGroups.CycleCreateGroup.class) CycleDto cycleDto);

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
    @DeleteMapping("/service/cycle/{id}")
    ResponseEntity<Void> deleteCycle(@PathVariable @Schema(description = "ID цикла") UUID id);
}
