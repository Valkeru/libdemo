package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.valkeru.libdemo.constants.ValidationConstants;

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

    @NotBlank(message = ValidationConstants.MSG_MANDATORY_FIELD)
    @Schema(description = "First name", example = "Mikhail")
    private String firstName;

    @Schema(description = "Middle name (names) or patronymic", example = "Afanas'evich")
    private String middleName;

    @NotBlank(message = ValidationConstants.MSG_MANDATORY_FIELD)
    @Schema(description = "Last name", example = "Bulgakov")
    private String lastName;
}
