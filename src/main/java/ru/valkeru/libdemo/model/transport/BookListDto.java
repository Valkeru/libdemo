package ru.valkeru.libdemo.model.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

import java.util.Collection;
import java.util.UUID;

public record BookListDto(
    @Schema(description = "ID объекта")
    UUID id,
    @Schema(description = "Название книги")
    String name,
    @Pattern(
        regexp = "\\d{3}-\\d-\\d{2}-\\d{6}-\\d"
    )
    String isbn,
    @Schema(description = "Имена авторов", example = "[\"Михаил Афанасьевич Булгаков\", \"Гарри Гаррисон\"]")
    Collection<String> authorsNames
) {
}
