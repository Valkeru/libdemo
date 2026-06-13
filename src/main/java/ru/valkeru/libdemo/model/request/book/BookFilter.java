package ru.valkeru.libdemo.model.request.book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookFilter {

    @Schema(description = "ISBN")
    private String isbn;

    @Schema(description = "Book title")
    private String name;

    @Schema(description = "Book author name")
    private String authorName;
}
