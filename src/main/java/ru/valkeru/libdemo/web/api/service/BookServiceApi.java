package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceListDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceViewDto;
import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

@SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(BookServiceApi.BOOK_SERVICE_PATH)
public interface BookServiceApi {

    String BOOK_SERVICE_PATH = "/service/book";

    @Operation(
        summary = "Add a book",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "201", ref = ApiConfig.REF_RESPONSE_RESOURCE_CREATED),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @PostMapping
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_CREATE.name())")
    ResponseEntity<Void> addBook(@Valid @RequestBody BookDto book);

    @Operation(
        summary = "Update a book data",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_UPDATE.name())")
    ResponseEntity<Void> updateBook(@PathVariable @Schema(description = "Book ID") UUID id,
                                    @Valid @RequestBody BookDto book);

    @Operation(
        summary = "Add a book copy",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "201", ref = ApiConfig.REF_RESPONSE_RESOURCE_CREATED),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_INSTANCE_CREATE.name())")
    @PostMapping("/instance")
    ResponseEntity<Void> addBookCopy(@Valid @RequestBody BookInstanceCreateDto dto);

    @Operation(
        summary = "View a book instances",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
        }
    )
    @GetMapping("/{bookId}/instances")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_INSTANCE_VIEW.name())")
    ResponseEntity<Page<BookInstanceListDto>> getBookInstances(@PathVariable UUID bookId,
                                                               @ParameterObject @PageableDefault Pageable pageable);

    @Operation(
        summary = "Get a book copy",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        },
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_INSTANCE_VIEW.name())")
    @GetMapping("/instance/{id}")
    ResponseEntity<BookInstanceViewDto> getBookCopy(@PathVariable UUID id);

    @Operation(
        summary = "Update a book copy",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_INSTANCE_UPDATE.name())")
    @PatchMapping("/instance/{id}")
    ResponseEntity<BookInstanceViewDto> updateBookCopy(@PathVariable UUID id,
                                                       @Valid @RequestBody BookInstancePatchDto dto,
                                                       @AuthenticationPrincipal LibraryPrincipal principal);

    @Operation(
        summary = "Delete a book",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).BOOK_DELETE.name())")
    ResponseEntity<Void> deleteBookById(@PathVariable @Schema(description = "Book ID") UUID id);
}
