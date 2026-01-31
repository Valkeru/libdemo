package ru.valkeru.libdemo.model.request.author;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorFilter {

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Имя")
    private String lastName;

    @Schema(description = "Имя")
    private String middleName;
}
