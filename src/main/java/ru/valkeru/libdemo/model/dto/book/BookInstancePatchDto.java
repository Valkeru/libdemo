package ru.valkeru.libdemo.model.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import ru.valkeru.libdemo.constants.ValidationConstants;

import java.util.List;

@Getter
@Setter
public class BookInstancePatchDto {

    @Schema(description = "Book inventory number", example = "00025134", requiredMode = Schema.RequiredMode.REQUIRED)
    private JsonNullable<@NotBlank(message = ValidationConstants.MSG_MANDATORY_FIELD) String> inventoryNumber
        = JsonNullable.undefined();

    @Schema(description = "Notes for book instance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private JsonNullable<List<@NotBlank(message = ValidationConstants.MSS_EMPTY_INVALID) String>> notes
        = JsonNullable.undefined();
}
