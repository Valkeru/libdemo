package ru.valkeru.libdemo.web.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.model.transport.BookListDto;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.config.api.ApiTags;

import java.util.UUID;

@RequestMapping(BookApi.BOOK_V1_PATH)
public interface BookApi {

    String BOOK_V1_PATH = "/v1/book";

    @Operation(
        summary = "Get books paged list",
        tags = ApiTags.BOOK
    )
    @GetMapping
    ResponseEntity<Page<BookListDto>> getAllBooks(@ParameterObject BookFilter filter,
                                                  @ParameterObject @PageableDefault Pageable pageable);

    @Operation(
        summary = "Get a book info",
        tags = ApiTags.BOOK,
        responses = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", ref = ApiConfig.REF_NOT_FOUND_RESPONSE)
        }
    )
    @GetMapping("/{id}")
    ResponseEntity<BookDto> getBookById(@PathVariable @Schema(description = "Book ID") UUID id);
}
