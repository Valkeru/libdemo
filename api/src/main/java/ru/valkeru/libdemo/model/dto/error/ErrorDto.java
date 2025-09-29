package ru.valkeru.libdemo.model.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Представление ошибки обработки запроса")
public record ErrorDto(
        @Schema(description = "HTTP статус", example = "NOT_FOUND")
        HttpStatus status,

        @Schema(description = "Сообщение об ошибке", example = "Данные не найдены")
        String message
) {
}
