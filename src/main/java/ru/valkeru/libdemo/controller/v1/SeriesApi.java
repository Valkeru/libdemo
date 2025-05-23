package ru.valkeru.libdemo.controller.v1;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.api.definition.ApiDefinition.SchemaIdDescription;
import ru.valkeru.libdemo.api.definition.ApiDefinition.StatusCodes;
import ru.valkeru.libdemo.api.definition.SeriesDefinition.Summary;
import ru.valkeru.libdemo.api.definition.SeriesDefinition.Tags;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.view.SeriesView;

import java.util.Collection;

@RestController
@RequestMapping("/v1/series")
public interface SeriesApi {

    @Operation(
            summary = Summary.Series.SUMMARY_CREATE,
            tags = Tags.SERIES,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.CREATED,
                            description = StatusCodes.Description.CREATED
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.BAD_REQUEST,
                            description = StatusCodes.Description.BAD_REQUEST,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.CONFLICT,
                            description = StatusCodes.Description.DATA_INTEGRITY_CONFLICT,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PostMapping
    @JsonView(SeriesView.SeriesSingleView.class)
    ResponseEntity<SeriesDto> createSeries(@RequestBody
                                           @Validated(SeriesView.SeriesCreateView.class)
                                           @JsonView(SeriesView.SeriesCreateView.class) SeriesDto series);

    @Operation(
            summary = Summary.Series.SUMMARY_UPDATE,
            tags = Tags.SERIES,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.OK,
                            description = StatusCodes.Description.OK
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.NOT_FOUND,
                            description = StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.CONFLICT,
                            description = StatusCodes.Description.DATA_INTEGRITY_CONFLICT
                    )
            }
    )
    @PatchMapping("/{seriesId}")
    @JsonView(SeriesView.SeriesSingleView.class)
    ResponseEntity<SeriesDto> updateSeries(@PathVariable(name = "seriesId")
                                           @Schema(description = SchemaIdDescription.SERIES_ID,
                                                   type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                                   format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id,
                                           @RequestBody
                                           @Validated(SeriesView.SeriesUpdateView.class)
                                           @JsonView(SeriesView.SeriesUpdateView.class) SeriesDto series);

    @GetMapping
    @Operation(
            summary = Summary.Series.SUMMARY_VIEW_LIST,
            tags = Tags.SERIES
    )
    @JsonView(SeriesView.SeriesListView.class)
    ResponseEntity<Collection<SeriesDto>> listAllSeries();

    @Operation(
            summary = Summary.Series.SUMMARY_VIEW,
            tags = Tags.SERIES,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.OK,
                            description = StatusCodes.Description.OK
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.NOT_FOUND,
                            description = StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @GetMapping("/{seriesId}")
    @JsonView(SeriesView.SeriesSingleView.class)
    ResponseEntity<SeriesDto> getSeries(@PathVariable(name = "seriesId")
                                        @Schema(description = SchemaIdDescription.SERIES_ID,
                                                type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                                format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id);

    @Operation(
            summary = Summary.Series.SUMMARY_DELETE,
            tags = Tags.SERIES,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.NO_CONTENT,
                            description = StatusCodes.Description.OK
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.NOT_FOUND,
                            description = StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.CONFLICT,
                            description = StatusCodes.Description.DATA_INTEGRITY_CONFLICT,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @DeleteMapping("/{seriesId}")
    ResponseEntity<Void> deleteSeries(@PathVariable(name = "seriesId")
                                      @Schema(description = SchemaIdDescription.SERIES_ID,
                                              type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                              format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id);
}
