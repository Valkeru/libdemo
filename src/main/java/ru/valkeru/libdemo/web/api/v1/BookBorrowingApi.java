package ru.valkeru.libdemo.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingDto;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

@RequestMapping("/v1/borrow")
@SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
@PreAuthorize("hasRole(T(ru.valkeru.libdemo.security.Role).ROLE_READER.roleName())")
public interface BookBorrowingApi {

    @Operation(
        summary = "Reserve a book to borrow",
        description = "Creates a reserve for a book. User can borrow it in a library later",
        tags = {ApiTags.BOOK},
        responses = {
            @ApiResponse(responseCode = "201", ref = ApiConfig.REF_RESPONSE_RESOURCE_CREATED),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @PostMapping("/{bookId}")
    ResponseEntity<Void> reserveBook(@PathVariable UUID bookId, @AuthenticationPrincipal LibraryPrincipal principal);

    @Operation(
        summary = "Cancel book reservation",
        tags = {ApiTags.BOOK},
        responses = {
            @ApiResponse(responseCode = "204", ref = ApiConfig.REF_RESPONSE_RESOURCE_CREATED),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> cancelReservation(@PathVariable UUID id);

    @Operation(
        summary = "Get borrowing info list for current user",
        tags = {ApiTags.BOOK},
        responses = {
            @ApiResponse(responseCode = "200", description = "Borrowing information"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
        }
    )
    @GetMapping
    ResponseEntity<Page<BookBorrowingListDto>> getBorrowingsList(@ParameterObject @PageableDefault Pageable pageable,
                                                                 @AuthenticationPrincipal LibraryPrincipal principal);

    @Operation(
        summary = "Get a borrow by following ID",
        description = "Returns a borrowing information",
        tags = {ApiTags.BOOK},
        responses = {
            @ApiResponse(responseCode = "200", description = "Borrowing information"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
        }
    )
    @GetMapping("/{id}")
    ResponseEntity<BookBorrowingDto> getBorrowing(@PathVariable UUID id,
                                                  @AuthenticationPrincipal LibraryPrincipal principal);
}
