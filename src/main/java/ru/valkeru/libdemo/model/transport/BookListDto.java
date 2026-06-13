package ru.valkeru.libdemo.model.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import java.util.Collection;
import java.util.UUID;

public record BookListDto(
    @Schema(description = "Object ID")
    UUID id,
    @Schema(description = "Book title")
    String name,
    @Pattern(
        regexp = "\\d{3}-\\d-\\d{2}-\\d{6}-\\d"
    )
    String isbn,
    @Schema(description = "Authors names", example = "[\"Mikhail Afanasievich Bulgakov\", \"Harry Harrison\"]")
    Collection<String> authorsNames
) {
}
