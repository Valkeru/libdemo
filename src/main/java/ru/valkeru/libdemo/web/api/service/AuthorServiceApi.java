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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.dto.error.FormFieldErrorDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

public interface AuthorServiceApi {

    String AUTHOR_SERVICE_URL = "/service/author";

    @Operation(
        summary = "Add an author info",
        tags = ApiTags.AUTHOR,
        responses = {
            @ApiResponse(responseCode = "201", description = "Success",
                headers = @Header(name = HttpHeaders.LOCATION, description = "Created resource location")
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = FormFieldErrorDto.class)))
                }
            ),
            @ApiResponse(responseCode = "409", description = "Data integrity violation", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            )
        },
        security = {
            @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
        }
    )
    @PostMapping("/service/author")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).AUTHOR_CREATE.name())")
    ResponseEntity<Void> createAuthor(@RequestBody @Valid AuthorDto author);

    @Operation(
        summary = "Update an author info",
        tags = ApiTags.AUTHOR,
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
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            )
        },
        security = {
            @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
        }
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).AUTHOR_UPDATE.name())")
    @PatchMapping("/service/author/{id}")
    ResponseEntity<Void> updateAuthor(@PathVariable @Schema(description = "Author ID") UUID id,
                                      @RequestBody @Valid AuthorDto author);

    @Operation(
        summary = "Delete an author",
        tags = ApiTags.AUTHOR,
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Success",
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
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Data integrity violation",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            )
        },
        security = {
            @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
        }
    )
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).AUTHOR_DELETE.name())")
    @DeleteMapping("/service/author/{id}")
    ResponseEntity<Void> deleteAuthor(@PathVariable @Schema(description = "Author ID") UUID id);
}
