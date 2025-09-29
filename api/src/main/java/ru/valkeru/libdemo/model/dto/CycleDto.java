package ru.valkeru.libdemo.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.valkeru.libdemo.model.view.BookView;
import ru.valkeru.libdemo.model.view.CycleView;
import ru.valkeru.libdemo.model.view.SeriesView;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "Цикл — несколько книг, объединённых общим сеттингом, но с разными персонажами")
public class CycleDto {

    @JsonView({
            CycleView.CycleSingleView.class,
            SeriesView.SeriesCreateView.class,
            SeriesView.SeriesSingleView.class,
            BookView.BookCreateView.class
    })
    @NotNull(groups = {
            SeriesView.SeriesCreateView.class
    })
    @Schema(description = "ID записи", example = "1")
    private Long id;

    @JsonView({
            CycleView.CycleCreateView.class,
            SeriesView.SeriesListView.class,
            BookView.BookSingleView.class,
            BookView.BookListView.class
    })
    @NotBlank(groups = {
            CycleView.CycleCreateView.class
    })
    @Schema(description = "Название", example = "Хроники Мидкемии")
    private String name;
}
