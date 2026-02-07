package ru.valkeru.libdemo.web.api.service;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.view.SeriesView;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.web.api.definition.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(SeriesServiceApi.SERIES_SERVICE_URL)
public interface SeriesServiceApi extends DefaultApi {

    String SERIES_SERVICE_URL = "/service/series";

    @Operation(
            summary = "Создать серию",
            tags = {ApiTags.SERIES, ApiTags.SERVICE},
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
    @JsonView(SeriesView.SeriesSingleView.class)
    default ResponseEntity<SeriesDto> createSeries(@RequestBody
                                           @Validated(SeriesView.SeriesCreateView.class)
                                           @JsonView(SeriesView.SeriesCreateView.class) SeriesDto series) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Обновить серию",
            tags = {ApiTags.SERIES, ApiTags.SERVICE},
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
                            responseCode = "409",
                            description = "Нарушение целостности данных"
                    )
            }
    )
    @PatchMapping("/{seriesId}")
    @JsonView(SeriesView.SeriesSingleView.class)
    default ResponseEntity<SeriesDto> updateSeries(@PathVariable(name = "seriesId")
                                           @Schema(description = "ID серии") UUID id,
                                           @RequestBody
                                           @Validated(SeriesView.SeriesUpdateView.class)
                                           @JsonView(SeriesView.SeriesUpdateView.class) SeriesDto series) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Удалить серию",
            tags = {ApiTags.SERIES, ApiTags.SERVICE},
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
    @DeleteMapping("/{seriesId}")
    default ResponseEntity<Void> deleteSeries(@PathVariable(name = "seriesId")
                                      @Schema(description = "ID серии") UUID id) {
        return defaultApiResponse();
    }
}
