package ru.valkeru.libdemo.model.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Simple request handling error")
public record ErrorDto(
        @Schema(description = "HTTP status", example = "NOT_FOUND")
        HttpStatus status,

        @Schema(description = "Error message", example = "Data is not exists")
        String message
) {
}
