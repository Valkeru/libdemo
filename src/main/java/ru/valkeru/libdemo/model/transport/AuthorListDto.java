package ru.valkeru.libdemo.model.transport;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AuthorListDto(
    @Schema(description = "Object ID")
    UUID id,
    @Schema(description = "Author name", example = "Mikhail Afanasievich Bulgakov")
    String fullName) {
}
