package ru.valkeru.libdemo.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.model.transport.AuthorListDto;
import ru.valkeru.libdemo.web.api.LibraryCommonApi;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@RequestMapping(AuthorApi.AUTHOR_V1_URL)
public interface AuthorApi extends LibraryCommonApi {

    String AUTHOR_V1_URL = "/v1/author";

    @Operation(
        summary = "Get authors paged list",
        tags = ApiTags.AUTHOR
    )
    @GetMapping
    default ResponseEntity<Page<AuthorListDto>> listAllAuthors(@ParameterObject AuthorFilter filter,
                                                               @ParameterObject @PageableDefault Pageable pageable) {
        return defaultApiResponse();
    }

    @Operation(
        summary = "Get an author info",
        tags = ApiTags.AUTHOR,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Data is not exists",
                content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
                }
            )
        }
    )
    @GetMapping("/{id}")
    default ResponseEntity<AuthorDto> getAuthor(@PathVariable
                                                @Schema(description = "Author ID") UUID id) {
        return defaultApiResponse();
    }
}
