package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Book")
public class BookDto {

    @Schema(description = "Object ID", example = "849ee885-e013-485f-a03f-de0be0403210")
    private UUID id;

    @Schema(description = "Title", example = "Another Fine Myth")
    private String name;

    @Size(max = 17, min = 17)
    @Schema(
            description = "<u>ISBN-13</u> ISBN",
            example = "978-5-17-049678-5"
    )
    @Pattern(
            regexp = "\\d{3}-\\d-\\d{2}-\\d{6}-\\d"
    )
    private String isbn;

    @Schema(description = "Authors")
    private Collection<AuthorDto> authors;

    @Schema(description = "Series")
    private SeriesDto series;

    @Schema(description = "Cycle")
    private CycleDto cycle;
}
