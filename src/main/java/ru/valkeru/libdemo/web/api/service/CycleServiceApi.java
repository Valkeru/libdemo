package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.infrastructure.validation.groups.CycleGroups;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(CycleServiceApi.CYCLE_SERVICE_PATH)
public interface CycleServiceApi {

    String CYCLE_SERVICE_PATH = "/service/cycle";

    @Operation(
        summary = "Add a cycle",
        tags = {ApiTags.CYCLE, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "201", ref = ApiConfig.REF_RESPONSE_RESOURCE_CREATED),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @PostMapping
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).CYCLE_CREATE.name())")
    ResponseEntity<Void> addCycle(@RequestBody @Validated(CycleGroups.CycleCreateGroup.class) CycleDto cycleDto);

    @Operation(
        summary = "Update a cycle data",
        tags = {ApiTags.CYCLE, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "Updated"),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).CYCLE_UPDATE.name())")
    ResponseEntity<Void> updateCycle(@PathVariable @Schema(description = "Cycle ID") UUID id,
                                     @RequestBody @Validated(CycleGroups.CycleCreateGroup.class) CycleDto cycleDto);

    @Operation(
        summary = "Remove a cycle",
        tags = {ApiTags.CYCLE, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).CYCLE_DELETE.name())")
    ResponseEntity<Void> deleteCycle(@PathVariable @Schema(description = "Cycle ID") UUID id);
}
