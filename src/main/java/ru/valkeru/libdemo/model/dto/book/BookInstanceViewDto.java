package ru.valkeru.libdemo.model.dto.book;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BookInstanceViewDto {

    @Schema(description = "Book instance ID")
    private UUID id;

    @Schema(description = "Related book ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID bookId;

    @Schema(description = "Book title", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "ISBN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String isbn;

    @Schema(description = "Authors", requiredMode = Schema.RequiredMode.REQUIRED)
    List<AuthorDto> authors = new ArrayList<>();

    @Schema(description = "Series", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private SeriesDto series;

    @Schema(description = "Cycle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private CycleDto cycle;

    @Schema(description = "Book inventory number", example = "00025134", requiredMode = Schema.RequiredMode.REQUIRED)
    private String inventoryNumber;

    @Schema(description = "Notes for book instance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonSetter(nulls = Nulls.SKIP, contentNulls = Nulls.SKIP)
    private List<String> notes = new ArrayList<>();
}
