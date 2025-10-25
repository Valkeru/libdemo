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
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.view.AuthorView;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.web.api.definition.ApiDefinition;
import ru.valkeru.libdemo.web.api.definition.AuthorDefinition;

import java.util.UUID;

@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
@RequestMapping(AuthorServiceApi.AUTHOR_SERVICE_URL)
public interface AuthorServiceApi extends DefaultApi {

    String AUTHOR_SERVICE_URL = "/service/author";

    /**
     * Создание записи об авторе
     */
    @Operation(
            summary = AuthorDefinition.Summary.Author.SUMMARY_CREATE,
            tags = AuthorDefinition.Tags.AUTHOR,
            responses = {
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.CREATED,
                            description = ApiDefinition.StatusCodes.Description.CREATED
                    ),
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.BAD_REQUEST,
                            description = ApiDefinition.StatusCodes.Description.BAD_REQUEST,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.CONFLICT,
                            description = ApiDefinition.StatusCodes.Description.DATA_INTEGRITY_CONFLICT,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PostMapping
    @JsonView(AuthorView.AuthorSingleView.class)
    default ResponseEntity<AuthorDto> createAuthor(@RequestBody
                                                   @Validated(AuthorView.AuthorCreateView.class)
                                                   @JsonView(AuthorView.AuthorCreateView.class) AuthorDto author) {
        return defaultApiResponse();
    }

    /**
     * Обновление записи об авторе
     */
    @Operation(
            summary = AuthorDefinition.Summary.Author.SUMMARY_UPDATE,
            tags = AuthorDefinition.Tags.AUTHOR,
            responses = {
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.OK,
                            description = ApiDefinition.StatusCodes.Description.OK
                    ),
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.NOT_FOUND,
                            description = ApiDefinition.StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.BAD_REQUEST,
                            description = ApiDefinition.StatusCodes.Description.BAD_REQUEST,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PatchMapping("/{id}")
    @JsonView(AuthorView.AuthorSingleView.class)
    default ResponseEntity<AuthorDto> updateAuthor(@PathVariable
                                                   @Schema(description = ApiDefinition.SchemaIdDescription.AUTHOR_ID) UUID id,
                                                   @RequestBody
                                                   @Validated(AuthorView.AuthorUpdateView.class)
                                                   @JsonView(AuthorView.AuthorUpdateView.class) AuthorDto author) {
        return defaultApiResponse();
    }

    /**
     * Удалить запись об авторе
     */
    @Operation(
            summary = AuthorDefinition.Summary.Author.SUMMARY_DELETE,
            tags = AuthorDefinition.Tags.AUTHOR,
            responses = {
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.NO_CONTENT,
                            description = ApiDefinition.StatusCodes.Description.OK,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.NOT_FOUND,
                            description = ApiDefinition.StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.CONFLICT,
                            description = ApiDefinition.StatusCodes.Description.DATA_INTEGRITY_CONFLICT,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @DeleteMapping("/{authorId}")
    default ResponseEntity<Void> deleteAuthor(@PathVariable(name = "authorId")
                                              @Schema(description = ApiDefinition.SchemaIdDescription.AUTHOR_ID) UUID id) {
        return defaultApiResponse();
    }
}
