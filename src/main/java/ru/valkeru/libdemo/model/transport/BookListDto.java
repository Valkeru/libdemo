package ru.valkeru.libdemo.model.transport;

import java.util.Collection;
import java.util.UUID;

public record BookListDto(
    UUID id,
    String name,
    String isbn,
    Collection<String> authorsNames
) {
}
