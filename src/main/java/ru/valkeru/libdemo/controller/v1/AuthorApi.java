package ru.valkeru.libdemo.controller.v1;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.api.definition.ApiDefinition.SchemaIdDescription;
import ru.valkeru.libdemo.api.definition.ApiDefinition.StatusCodes;
import ru.valkeru.libdemo.api.definition.AuthorDefinition.Summary;
import ru.valkeru.libdemo.api.definition.AuthorDefinition.Tags;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.model.view.AuthorView;

import java.util.Collection;

@RestController
@RequestMapping("/v1/author")
public interface AuthorApi {

    @Operation(
            summary = Summary.Author.SUMMARY_CREATE,
            tags = Tags.AUTHOR,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.CREATED,
                            description = StatusCodes.Description.CREATED
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.BAD_REQUEST,
                            description = StatusCodes.Description.BAD_REQUEST,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.CONFLICT,
                            description = StatusCodes.Description.DATA_INTEGRITY_CONFLICT,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PostMapping
    @JsonView(AuthorView.AuthorSingleView.class)
    ResponseEntity<AuthorDto> createAuthor(@RequestBody
                                           @Validated(AuthorView.AuthorCreateView.class)
                                           @JsonView(AuthorView.AuthorCreateView.class) AuthorDto author);

    @Operation(
            summary = Summary.Author.SUMMARY_UPDATE,
            tags = Tags.AUTHOR,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.OK,
                            description = StatusCodes.Description.OK
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.NOT_FOUND,
                            description = StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.BAD_REQUEST,
                            description = StatusCodes.Description.BAD_REQUEST,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PatchMapping("/{authorId}")
    @JsonView(AuthorView.AuthorSingleView.class)
    ResponseEntity<AuthorDto> updateAuthor(@PathVariable(name = "authorId")
                                           @Schema(description = SchemaIdDescription.AUTHOR_ID,
                                                   type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                                   format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id,
                                           @RequestBody
                                           @Validated(AuthorView.AuthorUpdateView.class)
                                           @JsonView(AuthorView.AuthorUpdateView.class) AuthorDto author);

    @Operation(
            summary = Summary.Author.SUMMARY_VIEW_LIST,
            tags = Tags.AUTHOR
    )
    @GetMapping
    @JsonView(AuthorView.AuthorListView.class)
    ResponseEntity<Collection<AuthorDto>> listAllAuthors(@ParameterObject AuthorFilter filter, @ParameterObject @PageableDefault Pageable pageable);

    @Operation(
            summary = Summary.Author.SUMMARY_VIEW,
            tags = Tags.AUTHOR,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.OK,
                            description = StatusCodes.Description.OK
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.NOT_FOUND,
                            description = StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @GetMapping("/{authorId}")
    @JsonView(AuthorView.AuthorSingleView.class)
    ResponseEntity<AuthorDto> getAuthor(@PathVariable(name = "authorId")
                                        @Schema(description = SchemaIdDescription.AUTHOR_ID,
                                                type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                                format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id);

    @Operation(
            summary = Summary.Author.SUMMARY_DELETE,
            tags = Tags.AUTHOR,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.NO_CONTENT,
                            description = StatusCodes.Description.OK,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.NOT_FOUND,
                            description = StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.CONFLICT,
                            description = StatusCodes.Description.DATA_INTEGRITY_CONFLICT,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @DeleteMapping("/{authorId}")
    ResponseEntity<Void> deleteAuthor(@PathVariable(name = "authorId")
                                      @Schema(description = SchemaIdDescription.AUTHOR_ID,
                                              type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                              format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id);

    @GetMapping("/apageable")
    ResponseEntity<Page<AuthorDto>> getPageableAuthors();
}
