package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Имя", example = "Михаил")
    private String firstName;

    @Schema(description = "Отчество или второе имя (имена)", example = "Афанасьевич")
    private String middleName;

    @Schema(description = "Фамилия", example = "Булгаков")
    private String lastName;
}
