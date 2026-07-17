package ru.valkeru.libdemo.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@RequestMapping(SeriesApi.SERIES_V1_PATH)
public interface SeriesApi {

    String SERIES_V1_PATH = "/v1/series";

    @Operation(
        summary = "Get series paged list",
        tags = ApiTags.SERIES
    )
    @GetMapping
    ResponseEntity<Page<SeriesDto>> listAllSeries(@ParameterObject @PageableDefault Pageable pageable);

    @Operation(
        summary = "Get a series data",
        tags = ApiTags.SERIES,
        responses = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @GetMapping("/{id}")
    ResponseEntity<SeriesDto> getSeries(@PathVariable @Schema(description = "Series ID") UUID id);
}
