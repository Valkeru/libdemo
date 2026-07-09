package ru.valkeru.libdemo.model.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Field validation error")
public record FormFieldErrorDto(
    @Schema(description = "Form field name", example = "title")
    String field,

    @Schema(description = "Field validation errors", example = "[\"must not be empty\"]")
    List<String> errors
) {

}
