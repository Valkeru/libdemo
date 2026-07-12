package ru.valkeru.libdemo.model.dto.book;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.constants.ValidationConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BookInstanceCreateDto {

    @Schema(description = "Book instance ID")
    private UUID id;

    @NotNull(message = ValidationConstants.MSG_MANDATORY_FIELD)
    @Schema(description = "Book ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID bookId;

    @NotBlank(message = ValidationConstants.MSG_MANDATORY_FIELD)
    @Schema(description = "Book inventory number", example = "00025134", requiredMode = Schema.RequiredMode.REQUIRED)
    private String inventoryNumber;

    @Schema(description = "Notes for book instance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonSetter(nulls = Nulls.SKIP, contentNulls = Nulls.SKIP)
    private List<@NotBlank(message = ValidationConstants.MSS_EMPTY_INVALID) String> notes = new ArrayList<>();
}
