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
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.web.api.definition.ApiTags;

import java.util.UUID;

@RequestMapping("/v1/cycle")
public interface CycleApi extends DefaultApi {

    @Operation(
            summary = "Получить все циклы",
            tags = ApiTags.CYCLE,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успех"
                    )
            }
    )
    @GetMapping
    default ResponseEntity<Page<CycleDto>> listCycles(@ParameterObject @PageableDefault Pageable pageable) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Получить цикл",
            tags = ApiTags.CYCLE,
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
    default ResponseEntity<CycleDto> getCycle(@PathVariable @Schema(description = "ID цикла") UUID id) {
        return defaultApiResponse();
    }
}
