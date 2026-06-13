package ru.valkeru.libdemo.exception.impl;

import org.apache.commons.lang3.StringUtils;
import ru.valkeru.libdemo.exception.NotFoundException;

import java.util.Collection;
import java.util.UUID;

public final class AuthorNotFoundException extends NotFoundException {

    private static final String AUTHOR_NOT_FOUND = "Author with ID %s not found";
    private static final String AUTHORS_NOT_FOUND = "Authors with ID %s was not found";

    private AuthorNotFoundException(String message) {
        super(message);
    }

    public static AuthorNotFoundException notFound(Collection<UUID> notFoundIds) {
        String message = notFoundIds.size() == 1
            ? AUTHOR_NOT_FOUND.formatted(notFoundIds.iterator().next())
            : AUTHORS_NOT_FOUND.formatted(StringUtils.join(notFoundIds, ", "));

        return new AuthorNotFoundException(message);
    }

    public static AuthorNotFoundException authorNotFound(UUID id) {
        return new AuthorNotFoundException(String.format(AUTHOR_NOT_FOUND, id));
    }
}
