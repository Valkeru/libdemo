package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

public interface BookServiceApi {

    @Operation(
        summary = "Add a book",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Success",
                headers = @Header(name = HttpHeaders.LOCATION)
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
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PostMapping("/service/book")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_CREATE.name())")
    ResponseEntity<Void> addBook(@RequestBody BookDto book);

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
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PatchMapping("/service/book/{id}")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_UPDATE.name())")
    ResponseEntity<Void> updateBook(@PathVariable @Schema(description = "Book ID") UUID id,
                                    @RequestBody BookDto book);

    @Operation(
        summary = "Add a book copy",
        tags = { ApiTags.BOOK, ApiTags.SERVICE },
        responses = @ApiResponse(responseCode = "201", description = "Success", headers = {
            @Header(name = HttpHeaders.LOCATION)
        })
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_INSTANCE_CREATE.name())")
    @PostMapping("/service/book/add-copy")
    ResponseEntity<Void> addBookCopy();

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
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @DeleteMapping("/service/book/{id}")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_DELETE.name())")
    ResponseEntity<Void> deleteBookById(@PathVariable @Schema(description = "Book ID") UUID id);
}
