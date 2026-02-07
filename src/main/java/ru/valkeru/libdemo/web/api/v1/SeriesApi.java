package ru.valkeru.libdemo.web.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.view.SeriesView;
import ru.valkeru.libdemo.web.api.definition.ApiTags;

import java.util.List;
import java.util.UUID;

@RequestMapping("/v1/series")
public interface SeriesApi extends DefaultApi {

    @GetMapping
    @Operation(
            summary = "Получить все серии",
            tags = ApiTags.SERIES
    )
    @JsonView(SeriesView.SeriesListView.class)
    default ResponseEntity<List<SeriesDto>> listAllSeries() {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Данные о серии",
            tags = ApiTags.SERIES,
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
                    )
            }
    )
    @GetMapping("/{id}")
    @JsonView(SeriesView.SeriesSingleView.class)
    default ResponseEntity<SeriesDto> getSeries(@PathVariable @Schema(description = "ID серии") UUID id) {
        return defaultApiResponse();
    }
}
