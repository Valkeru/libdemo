package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.web.api.LibraryCommonApi;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(BookServiceApi.BOOK_SERVICE_URL)
public interface BookServiceApi extends LibraryCommonApi {

    String BOOK_SERVICE_URL = "/service/book";

    @Operation(
        summary = "Add a book",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Success",
                headers = @Header(name = CustomHeaders.RESOURCE_ID, ref = OpenApiConfig.RESOURCE_ID_HEADER_REF)
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
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
    @PostMapping
    default ResponseEntity<Void> addBook(@RequestBody BookDto book) {
        return defaultApiResponse();
    }

    @Operation(
        summary = "Update a book data",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Success"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))}
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
    @PatchMapping("/{bookId}")
    default ResponseEntity<Void> updateBook(@PathVariable(name = "bookId")
                                               @Schema(description = "Book ID") UUID id,
                                               @RequestBody BookDto book) {
        return defaultApiResponse();
    }

    @Operation(
        summary = "Add a book copy",
        tags = { ApiTags.BOOK, ApiTags.SERVICE },
        responses = @ApiResponse(responseCode = "202", description = "Success", headers = {
            @Header(name = CustomHeaders.RESOURCE_ID, ref = OpenApiConfig.RESOURCE_ID_HEADER_REF)
        })
    )
    @PostMapping("/add-copy")
    default ResponseEntity<Void> addBookCopy() {
        return defaultApiResponse();
    }

    @Operation(
        summary = "Delete a book",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "204",
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
    @DeleteMapping("/{bookId}")
    default ResponseEntity<Void> deleteBookById(@PathVariable(name = "bookId")
                                                @Schema(description = "Book ID") UUID id) {
        return defaultApiResponse();
    }
}
