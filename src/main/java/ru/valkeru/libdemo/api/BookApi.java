package ru.valkeru.libdemo.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import ru.valkeru.libdemo.api.definition.ApiDefinition.Summary;
import ru.valkeru.libdemo.api.definition.ApiDefinition.Tags;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.dto.error.ErrorDto;
import ru.valkeru.libdemo.model.view.BookView;

import java.util.Collection;

@RestController
@RequestMapping("/books")
public interface BookApi {

    @Operation(
            summary = Summary.Book.SUMMARY_CREATE,
            tags = Tags.BOOK,
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
                            responseCode = StatusCodes.NOT_FOUND,
                            description = StatusCodes.Description.NOT_FOUND,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))
                            }
                    )
            }
    )
    @PostMapping
    @JsonView(BookView.BookSingleView.class)
    ResponseEntity<BookDto> addBook(@RequestBody
                                    @Validated(BookView.BookCreateView.class)
                                    @JsonView(BookView.BookCreateView.class) BookDto book);

    @Operation(
            summary = Summary.Book.SUMMARY_UPDATE,
            tags = Tags.BOOK,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.OK,
                            description = StatusCodes.Description.OK
                    ),
                    @ApiResponse(
                            responseCode = StatusCodes.BAD_REQUEST,
                            description = StatusCodes.Description.BAD_REQUEST,
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorDto.class))}
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
    @PatchMapping("/{bookId}")
    @JsonView(BookView.BookSingleView.class)
    ResponseEntity<BookDto> updateBook(@PathVariable(name = "bookId")
                                       @Schema(description = SchemaIdDescription.BOOK_ID,
                                               type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                               format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id,
                                       @RequestBody
                                       @Validated(BookView.BookUpdateView.class)
                                       @JsonView(BookView.BookUpdateView.class) BookDto book);

    @Operation(
            summary = Summary.Book.SUMMARY_VIEW_LIST,
            tags = Tags.BOOK
    )
    @GetMapping
    @JsonView(BookView.BookListView.class)
    ResponseEntity<Collection<BookDto>> getAllBooks();

    @Operation(
            summary = Summary.Book.SUMMARY_VIEW,
            tags = Tags.BOOK,
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
    @GetMapping("/{bookId}")
    @JsonView(BookView.BookSingleView.class)
    ResponseEntity<BookDto> getBookById(@PathVariable(name = "bookId")
                                        @Schema(description = SchemaIdDescription.BOOK_ID,
                                                type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                                format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id);

    @Operation(
            summary = Summary.Book.SUMMARY_DELETE,
            tags = Tags.BOOK,
            responses = {
                    @ApiResponse(
                            responseCode = StatusCodes.NO_CONTENT,
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
    @DeleteMapping("/{bookId}")
    ResponseEntity<Void> deleteBookById(@PathVariable(name = "bookId")
                                        @Schema(description = SchemaIdDescription.BOOK_ID,
                                                type = SchemaIdDescription.DEFAULT_PATH_ID_TYPE,
                                                format = SchemaIdDescription.DEFAULT_PATH_ID_FORMAT) Long id);
}
