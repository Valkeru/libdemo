package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "Цикл — несколько книг, объединённых общим сеттингом, но с разным сюжетом")
public class CycleDto {

    @Schema(description = "ID записи", example = "8e468a24-1cb5-4564-8b91-8ccda21cbce2")
    private UUID id;

    @Schema(description = "Название", example = "Хроники Мидкемии")
    private String name;
}
