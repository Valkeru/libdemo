package ru.valkeru.libdemo.persistence.projection;

import java.util.Collection;
import java.util.UUID;

public record BookShortProjection(
    UUID id,
    String name,
    String isbn,
    Collection<String> authorsNames
) {
}
