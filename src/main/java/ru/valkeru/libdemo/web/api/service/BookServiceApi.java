package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceViewDto;
import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.model.dto.error.FormFieldErrorDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

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
        responses = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = {
                @Content(array = @ArraySchema(schema = @Schema(implementation = FormFieldErrorDto.class)))
            }),
            @ApiResponse(responseCode = "401", description = "Authentication required", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                @Content(schema = @Schema(implementation = ErrorDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(schema = @Schema(implementation = ErrorDto.class))
            })
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_INSTANCE_CREATE.name())")
    @PostMapping("/service/book/instance")
    ResponseEntity<Void> addBookCopy(@Valid @RequestBody BookInstanceCreateDto dto);

    @Operation(
        summary = "Get a book copy",
        tags = { ApiTags.BOOK, ApiTags.SERVICE },
        responses = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Authentication required", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                @Content(schema = @Schema(implementation = ErrorDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(schema = @Schema(implementation = ErrorDto.class))
            })
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_INSTANCE_VIEW.name())")
    @GetMapping("/service/book/instance/{id}")
    ResponseEntity<BookInstanceViewDto> getBookCopy(@PathVariable UUID id);

    @Operation(
        summary = "Update a book copy",
        tags = { ApiTags.BOOK, ApiTags.SERVICE },
        responses = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = {
                @Content(array = @ArraySchema(schema = @Schema(implementation = FormFieldErrorDto.class)))
            }),
            @ApiResponse(responseCode = "401", description = "Authentication required", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                @Content(schema = @Schema(implementation = ErrorDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                @Content(schema = @Schema(implementation = ErrorDto.class))
            })
        },
        security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_INSTANCE_UPDATE.name())")
    @PatchMapping("/service/book/instance/{id}")
    ResponseEntity<Void> updateBookCopy(@PathVariable UUID id, @Valid @RequestBody BookInstancePatchDto dto,
                                        @AuthenticationPrincipal LibraryPrincipal principal);

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
