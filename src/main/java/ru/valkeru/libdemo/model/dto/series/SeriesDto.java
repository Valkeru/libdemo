package ru.valkeru.libdemo.model.dto.series;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.valkeru.libdemo.model.dto.CycleDto;

import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "A series is several books with a common setting and characters. Series may be a part of a cycle")
public class SeriesDto {

    @Schema(description = "Object ID", example = "bea5db6f-0c17-471d-a3f4-2c348560adea")
    private UUID id;

    @Schema(description = "Title", example = "The Stainless Steel Rat")
    private String title;

    @Schema(description = "Cycle ID")
    private CycleDto cycle;
}
