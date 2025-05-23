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
import ru.valkeru.libdemo.api.definition.ApiDefinition.Summary;
import ru.valkeru.libdemo.api.definition.ApiDefinition.Tags;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.view.CycleView;

import java.util.Collection;

@RestController
@RequestMapping("/v1/cycle")
public interface CycleApi {

    @Operation(
            summary = Summary.Cycle.SUMMARY_CREATE,
            tags = Tags.CYCLE,
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
    @JsonView(CycleView.CycleSingleView.class)
    ResponseEntity<CycleDto> addCycle(@RequestBody
                                      @Validated(CycleView.CycleCreateView.class)
                                      @JsonView(CycleView.CycleCreateView.class) CycleDto cycleDto);

    @Operation(
            summary = Summary.Cycle.SUMMARY_UPDATE,
            tags = Tags.CYCLE,
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
    @PatchMapping("/{cycleId}")
    @JsonView(CycleView.CycleListView.class)
    ResponseEntity<CycleDto> updateCycle(@PathVariable(name = "cycleId")
                                         @Schema(description = SchemaIdDescription.CYCLE_ID,
                                                 type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                                 format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id,
                                         @RequestBody
                                         @Validated(CycleView.CycleUpdateView.class)
                                         @JsonView(CycleView.CycleUpdateView.class) CycleDto cycleDto);

    @Operation(
            summary = Summary.Cycle.SUMMARY_VIEW_LIST,
            tags = Tags.CYCLE,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.OK,
                            description = StatusCodes.Description.OK
                    )
            }
    )
    @GetMapping
    @JsonView(CycleView.CycleSingleView.class)
    ResponseEntity<Collection<CycleDto>> listCycles();

    @Operation(
            summary = Summary.Cycle.SUMMARY_VIEW,
            tags = Tags.CYCLE,
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
    @GetMapping("/{cycleId}")
    @JsonView(CycleView.CycleSingleView.class)
    ResponseEntity<CycleDto> getCycle(@PathVariable(name = "cycleId")
                                      @Schema(description = SchemaIdDescription.CYCLE_ID,
                                              type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                              format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id);

    @Operation(
            summary = Summary.Cycle.SUMMARY_DELETE,
            tags = Tags.CYCLE,
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
    @DeleteMapping("/{cycleId}")
    ResponseEntity<Void> deleteCycle(@PathVariable(name = "cycleId")
                                     @Schema(description = SchemaIdDescription.CYCLE_ID,
                                             type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                             format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id);
}
