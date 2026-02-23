package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.web.api.definition.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(CycleServiceApi.CYCLE_SERVICE_URL)
public interface CycleServiceApi extends DefaultApi {

    String CYCLE_SERVICE_URL = "/service/cycle";

    @Operation(
            summary = "Добавить цикл",
            tags = {ApiTags.CYCLE, ApiTags.SERVICE},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Успех"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректный запрос",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Нарушение целостности данных",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PostMapping
    default ResponseEntity<CycleDto> addCycle(@RequestBody CycleDto cycleDto) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Обновить цикл",
            tags = {ApiTags.CYCLE, ApiTags.SERVICE},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успех"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Данные не найдены",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректный запрос",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Нарушение целостности данных",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PatchMapping("/{cycleId}")
    default ResponseEntity<CycleDto> updateCycle(@PathVariable(name = "cycleId")
                                         @Schema(description = "ID цикла") UUID id,
                                         @RequestBody CycleDto cycleDto) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Удалить цикл",
            tags = {ApiTags.CYCLE, ApiTags.SERVICE},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Успех"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Данные не найдены",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Нарушение целостности данных",
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
