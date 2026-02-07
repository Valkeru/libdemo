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
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.view.BookView;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.web.api.definition.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(BookServiceApi.BOOK_SERVICE_URL)
public interface BookServiceApi extends DefaultApi {

    String BOOK_SERVICE_URL = "/service/book";

    @Operation(
            summary = "Добавить книгу",
            tags = {ApiTags.BOOK, ApiTags.SERVICE},
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
                            responseCode = "404",
                            description = "Данные не найдены",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PostMapping
    @JsonView(BookView.BookSingleView.class)
    default ResponseEntity<BookDto> addBook(@RequestBody
                                    @Validated(BookView.BookCreateView.class)
                                    @JsonView(BookView.BookCreateView.class) BookDto book) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Обновить данные о книге",
            tags = {ApiTags.BOOK, ApiTags.SERVICE},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успех"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректный запрос",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))}
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
    @PatchMapping("/{bookId}")
    @JsonView(BookView.BookSingleView.class)
    default ResponseEntity<BookDto> updateBook(@PathVariable(name = "bookId")
                                       @Schema(description = "ID книги") UUID id,
                                       @RequestBody
                                       @Validated(BookView.BookUpdateView.class)
                                       @JsonView(BookView.BookUpdateView.class) BookDto book) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Удалить книгу",
            tags = {ApiTags.BOOK, ApiTags.SERVICE},
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
                    )
            }
    )
    @DeleteMapping("/{bookId}")
    default ResponseEntity<Void> deleteBookById(@PathVariable(name = "bookId")
                                        @Schema(description = "ID книги") UUID id) {
        return defaultApiResponse();
    }
}
