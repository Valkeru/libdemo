package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "A cycle is several books with a common setting but differs in a plot")
public class CycleDto {

    @Schema(description = "Object ID", example = "8e468a24-1cb5-4564-8b91-8ccda21cbce2")
    private UUID id;

    @NotBlank
    @Schema(description = "Title", example = "Chronicles of Midkemia")
    private String name;
}
