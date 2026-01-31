package ru.valkeru.libdemo.web.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.web.api.definition.ApiDefinition.SchemaIdDescription;
import ru.valkeru.libdemo.web.api.definition.ApiDefinition.StatusCodes;
import ru.valkeru.libdemo.web.api.definition.AuthorDefinition.Summary;
import ru.valkeru.libdemo.web.api.definition.AuthorDefinition.Tags;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.model.view.AuthorView;

import java.util.UUID;

@RequestMapping(AuthorApi.AUTHOR_V1_URL)
public interface AuthorApi extends DefaultApi {

    String AUTHOR_V1_URL = "/v1/author";

    /**
     * Постраничный просмотр списка авторов
     */
    @Operation(
            summary = Summary.Author.SUMMARY_VIEW_LIST,
            tags = Tags.AUTHOR
    )
    @GetMapping
    @JsonView(AuthorView.AuthorListView.class)
    default ResponseEntity<PagedModel<AuthorDto>> listAllAuthors(@ParameterObject AuthorFilter filter,
                                                                 @ParameterObject @PageableDefault Pageable pageable) {
        return defaultApiResponse();
    }

    /**
     * Получить запись об авторе по id
     */
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
    default ResponseEntity<AuthorDto> getAuthor(@PathVariable(name = "authorId")
                                                @Schema(description = SchemaIdDescription.AUTHOR_ID) UUID id) {
        return defaultApiResponse();
    }
}
