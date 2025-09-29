package ru.valkeru.libdemo.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.valkeru.libdemo.model.view.BookView;

import java.util.Collection;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Книга")
public class BookDto {

    @JsonView({
            BookView.BookListView.class,
            BookView.BookSingleView.class
    })
    @NotNull
    @Schema(description = "ID записи", example = "1")
    private Long id;

    @JsonView({
            BookView.BookCreateView.class,
            BookView.BookListView.class
    })
    @NotNull
    @Schema(description = "Название", example = "Ещё один великолепный МИФ")
    private String name;

    @JsonView({
            BookView.BookCreateView.class
    })
    @NotNull(groups = {
            BookView.BookCreateView.class
    })
    @Valid
    @Size(max = 17, min = 17)
    @Schema(
            description = "ISBN формата <u>ISBN-13</u>",
            example = "978-5-17-049678-5"
    )
    @Pattern(
            regexp = "\\d{3}-\\d-\\d{2}-\\d{6}-\\d",
            groups = {
                    BookView.BookCreateView.class
            }
    )
    private String isbn;

    @JsonView({
            BookView.BookCreateView.class,
            BookView.BookListView.class
    })
    @NotNull(groups = {
            BookView.BookCreateView.class
    })
    @Valid
    @Schema(description = "Авторы")
    private Collection<AuthorDto> authors;

    @JsonView({
            BookView.BookCreateView.class,
            BookView.BookListView.class
    })
    @Valid
    @Schema(description = "Серия")
    private SeriesDto series;

    @JsonView({
            BookView.BookCreateView.class,
            BookView.BookListView.class
    })
    @Valid
    @Schema(description = "Цикл")
    private CycleDto cycle;
}
