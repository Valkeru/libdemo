package ru.valkeru.libdemo.model.dto.series;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.constants.ValidationConstants;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Series edit schema")
public class SeriesEditDto {

    @NotBlank(message = ValidationConstants.MSG_MANDATORY_FIELD)
    @Schema(description = "Title", example = "The Stainless Steel Rat")
    private String title;

    @Schema(description = "Cycle ID")
    private UUID cycleId;
}
