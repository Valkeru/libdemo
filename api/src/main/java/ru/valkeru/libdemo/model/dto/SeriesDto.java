package ru.valkeru.libdemo.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.valkeru.libdemo.model.view.BookView;
import ru.valkeru.libdemo.model.view.SeriesView;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Серия — несколько книг, объединённых общим сеттингом и персонажами")
public class SeriesDto {

    @NotNull(groups = {
            BookView.BookCreateView.class
    })
    @Schema(description = "ID записи", example = "1")
    @JsonView({
            SeriesView.SeriesListView.class,
            BookView.BookCreateView.class
    })
    private Long id;

    @NotBlank(groups = {
            SeriesView.SeriesCreateView.class
    })
    @Schema(description = "Название", example = "Стальная Крыса")
    @JsonView({
            SeriesView.SeriesCreateView.class,
            SeriesView.SeriesListView.class,
            BookView.BookListView.class,
            BookView.BookSingleView.class
    })
    private String name;

    @JsonView({
            SeriesView.SeriesCreateView.class,
            SeriesView.SeriesListView.class
    })
    @Valid
    @Schema(description = "Цикл")
    private CycleDto cycle;
}
