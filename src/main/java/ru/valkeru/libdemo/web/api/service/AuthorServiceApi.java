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
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.web.api.LibraryCommonApi;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(AuthorServiceApi.AUTHOR_SERVICE_URL)
public interface AuthorServiceApi extends LibraryCommonApi {

    String AUTHOR_SERVICE_URL = "/service/author";

    @Operation(
        summary = "Add an author info",
        tags = ApiTags.AUTHOR,
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
                responseCode = "409",
                description = "Data integrity violation",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            )
        }
    )
    @PostMapping
    default ResponseEntity<Void> createAuthor(@RequestBody AuthorDto author) {
        return defaultApiResponse();
    }

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
        }
    )
    @PatchMapping("/{id}")
    default ResponseEntity<Void> updateAuthor(@PathVariable
                                              @Schema(description = "Author ID") UUID id,
                                              @RequestBody AuthorDto author) {
        return defaultApiResponse();
    }

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
        }
    )
    @DeleteMapping("/{authorId}")
    default ResponseEntity<Void> deleteAuthor(@PathVariable(name = "authorId")
                                              @Schema(description = "Author ID") UUID id) {
        return defaultApiResponse();
    }

    @Operation(
        summary = "Reindex authors in Elasticsearch",
        tags = ApiTags.SERVICE,
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Task is created"
            )
        }
    )
    @PostMapping("/reindex")
    default ResponseEntity<Void> elasticsearchReindexAuthors() {
        return defaultApiResponse();
    }
}
