package ru.valkeru.libdemo.model.dto.book;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.valkeru.libdemo.component.jackson.ISBNDeserializer;
import ru.valkeru.libdemo.constants.ValidationConstants;
import ru.valkeru.libdemo.infrastructure.validation.annotation.ValidISBN13;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Book")
public class BookDto {

    @Schema(description = "Object ID", example = "849ee885-e013-485f-a03f-de0be0403210")
    private UUID id;

    @NotBlank(message = ValidationConstants.MSG_MANDATORY_FIELD)
    @Schema(description = "Title", example = "Another Fine Myth")
    private String title;

    @NotBlank(message = ValidationConstants.MSG_MANDATORY_FIELD)
    @Size(max = 17, min = 17, message = "Invalid ISBN length")
    @Schema(
        description = "<u>ISBN-13</u> ISBN",
        example = "978-5-17-049678-5"
    )
    @ValidISBN13
    @JsonDeserialize(using = ISBNDeserializer.class)
    private String isbn;

    @JsonSetter(nulls = Nulls.SKIP)
    @Size(min = 1, message = "At least 1 author is required")
    @Schema(description = "Authors")
    private Collection<AuthorDto> authors = new ArrayList<>();

    @Schema(description = "Series")
    private SeriesDto series;

    @Schema(description = "Cycle")
    private CycleDto cycle;
}
