package ru.valkeru.libdemo.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

public interface CycleApi {

    @Operation(
        summary = "Get cycles paged list",
        tags = ApiTags.CYCLE,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success"
            )
        }
    )
    @GetMapping("/v1/cycle")
    ResponseEntity<Page<CycleDto>> listCycles(@ParameterObject @PageableDefault Pageable pageable);

    @Operation(
        summary = "Get a cycle data",
        tags = ApiTags.CYCLE,
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
    @GetMapping("/v1/cycle/{id}")
    ResponseEntity<CycleDto> getCycle(@PathVariable @Schema(description = "ID цикла") UUID id);
}
