package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.model.dto.lending.BookLendingDto;
import ru.valkeru.libdemo.model.dto.lending.BookLendingListDto;
import ru.valkeru.libdemo.model.request.lending.ServiceLendingFilter;

import java.util.UUID;

@RequestMapping("/service/lending")
@SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
public interface BookLendingServiceApi {

    @Operation(
        summary = "List lendings for a user with particular card number",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        description = """
            List lendings for user using library card number.
            All lendings will be listed including lendings with any previous user's library card
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", ref = ApiConfig.REF_VALIDATION_ERROR_RESPONSE),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
        }
    )
    @GetMapping
    ResponseEntity<Page<BookLendingListDto>> listLendings(@Valid @ParameterObject ServiceLendingFilter filter,
                                                          @ParameterObject @PageableDefault Pageable pageable);

    @Operation(
        summary = "Create a book reservation",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "201", ref = ApiConfig.REF_RESPONSE_RESOURCE_CREATED),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @PreAuthorize("hasRole(T(ru.valkeru.libdemo.security.Role).LIBRARIAN)")
    @PostMapping("/{bookId}/reserve")
    ResponseEntity<Void> reserveBook(@PathVariable UUID bookId,
                                     @Parameter(in = ParameterIn.PATH) UUID libraryCardId);

    @Operation(
        summary = "Get a book lending data",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(T(ru.valkeru.libdemo.security.Role).LIBRARIAN)")
    ResponseEntity<BookLendingDto> getLending(@PathVariable UUID id);

    @Operation(
        summary = "Issue a reserved book",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "OK"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @PreAuthorize("hasRole(T(ru.valkeru.libdemo.security.Role).LIBRARIAN)")
    @PostMapping("/{id}/issue")
    ResponseEntity<Void> issueReservedBook(@PathVariable
                                           @Parameter(
                                               description = "Lending id (RESERVED status is required)",
                                               required = true
                                           ) UUID id);

    @Operation(
        summary = "Cancel a book reservation",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "OK"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).SERVICE_BOOK_LENDING_CANCEL.name())")
    @PostMapping("/{id}/cancel")
    ResponseEntity<Void> cancelReserve(@PathVariable
                                        @Parameter(
                                            required = true,
                                            description = "Lending id (RESERVED status is required)"
                                        )
                                        UUID id);

    @Operation(
        summary = "Return a borrowed book to a library",
        tags = {ApiTags.BOOK, ApiTags.SERVICE},
        responses = {
            @ApiResponse(responseCode = "204", description = "OK"),
            @ApiResponse(responseCode = "401", ref = ApiConfig.REF_UNAUTHORIZED_RESPONSE),
            @ApiResponse(responseCode = "403", ref = ApiConfig.REF_ACCESS_DENIED_RESPONSE),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE),
            @ApiResponse(responseCode = "409", ref = ApiConfig.REF_INTEGRITY_VIOLATION_RESPONSE)
        }
    )
    @PreAuthorize("hasRole(T(ru.valkeru.libdemo.security.Role).LIBRARIAN)")
    @PostMapping("/{lendingId}/return")
    ResponseEntity<Void> returnBook(@PathVariable
                                    @Parameter(
                                        required = true,
                                        description = "Lending id (BORROWED status is required)"
                                    )
                                    UUID lendingId);
}
