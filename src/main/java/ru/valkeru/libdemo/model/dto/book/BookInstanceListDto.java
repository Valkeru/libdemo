package ru.valkeru.libdemo.model.dto.book;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BookInstanceListDto {

    @Schema(description = "Book instance ID")
    private UUID id;

    @Schema(description = "Book inventory number", example = "00025134", requiredMode = Schema.RequiredMode.REQUIRED)
    private String inventoryNumber;

    @Schema(description = "Notes for book instance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonSetter(nulls = Nulls.SKIP, contentNulls = Nulls.SKIP)
    private List<String> notes = new ArrayList<>();
}
