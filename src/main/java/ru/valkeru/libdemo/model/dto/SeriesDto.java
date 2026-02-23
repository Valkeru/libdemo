package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Серия — несколько книг, объединённых общим сеттингом и персонажами")
public class SeriesDto {

    @Schema(description = "ID записи", example = "bea5db6f-0c17-471d-a3f4-2c348560adea")
    private UUID id;

    @Schema(description = "Название", example = "Стальная Крыса")
    private String name;

    @Schema(description = "Цикл")
    private CycleDto cycle;
}
