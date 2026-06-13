package ru.valkeru.libdemo.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.model.transport.BookListDto;
import ru.valkeru.libdemo.web.api.LibraryCommonApi;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@RequestMapping("/v1/books")
public interface BookApi extends LibraryCommonApi {

    @Operation(
            summary = "Get books paged list",
            tags = ApiTags.BOOK
    )
    @GetMapping
    default ResponseEntity<Page<BookListDto>> getAllBooks(@ParameterObject BookFilter filter,
                                                          @ParameterObject
                                                          @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                                          Pageable pageable) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Get a book info",
            tags = ApiTags.BOOK,
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
    default ResponseEntity<BookDto> getBookById(@PathVariable @Schema(description = "Book ID") UUID id) {
        return defaultApiResponse();
    }
}
