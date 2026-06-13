package ru.valkeru.libdemo.model.request.author;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorFilter {

    @Schema(description = "Author first name")
    private String firstName;

    @Schema(description = "Author last name")
    private String lastName;

    @Schema(description = "Author middle name or names")
    private String middleName;
}
