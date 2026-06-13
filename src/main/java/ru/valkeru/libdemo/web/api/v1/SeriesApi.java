package ru.valkeru.libdemo.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.web.api.LibraryCommonApi;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.List;
import java.util.UUID;

@RequestMapping(SeriesApi.SERIES_V1_URL)
public interface SeriesApi extends LibraryCommonApi {

    String SERIES_V1_URL = "/v1/series";

    @GetMapping
    @Operation(
            summary = "Get series paged list",
            tags = ApiTags.SERIES
    )
    default ResponseEntity<List<SeriesDto>> listAllSeries() {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Get a series data",
            tags = ApiTags.SERIES,
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
                    )
            }
    )
    @GetMapping("/{id}")
    default ResponseEntity<SeriesDto> getSeries(@PathVariable @Schema(description = "Series ID") UUID id) {
        return defaultApiResponse();
    }
}
