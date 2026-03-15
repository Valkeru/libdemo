package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Автор")
public class AuthorDto {

    @Schema(description = "ID записи")
    private UUID id;

    @NotBlank
    @Schema(description = "Имя", example = "Михаил")
    private String firstName;

    @Schema(description = "Отчество или второе имя (имена)", example = "Афанасьевич")
    private String middleName;

    @NotBlank
    @Schema(description = "Фамилия", example = "Булгаков")
    private String lastName;
}
