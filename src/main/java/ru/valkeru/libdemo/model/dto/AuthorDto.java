package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Author")
public class AuthorDto {

    @Schema(description = "Object ID")
    private UUID id;

    @NotBlank
    @Schema(description = "First name", example = "Mikhail")
    private String firstName;

    @Schema(description = "Middle name (names) or patronymic", example = "Afanasievich")
    private String middleName;

    @NotBlank
    @Schema(description = "Last name", example = "Bulgakov")
    private String lastName;
}
