package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.web.api.definition.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(AuthorServiceApi.AUTHOR_SERVICE_URL)
public interface AuthorServiceApi extends DefaultApi {

    String AUTHOR_SERVICE_URL = "/service/author";

    /**
     * Создание записи об авторе
     */
    @Operation(
            summary = "Добавить данные об авторе",
            tags = ApiTags.AUTHOR,
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
    default ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        return defaultApiResponse();
    }

    /**
     * Обновление записи об авторе
     */
    @Operation(
            summary = "Обновить данные об авторе",
            tags = ApiTags.AUTHOR,
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
                    )
            }
    )
    @PatchMapping("/{id}")
    default ResponseEntity<AuthorDto> updateAuthor(@PathVariable
                                                   @Schema(description = "ID автора") UUID id,
                                                   @RequestBody AuthorDto author) {
        return defaultApiResponse();
    }

    /**
     * Удалить запись об авторе
     */
    @Operation(
            summary = "Удалить данные об авторе",
            tags = ApiTags.AUTHOR,
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Успех",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
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
    @DeleteMapping("/{authorId}")
    default ResponseEntity<Void> deleteAuthor(@PathVariable(name = "authorId")
                                              @Schema(description = "ID автора") UUID id) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Переиндексировать авторов в Elasticsearch",
            tags = ApiTags.SERVICE,
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Задание создано"
                    )
            }
    )
    @PostMapping("/reindex")
    default ResponseEntity<Void> elasticsearchReindexAuthors() {
        return defaultApiResponse();
    }
}
