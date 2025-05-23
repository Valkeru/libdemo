package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.NotFoundException;

import java.util.Collection;

public final class AuthorNotFoundException extends NotFoundException {

    private static final String AUTHOR_NOT_FOUND = "Автор с ID %d не найден";
    private static final String AUTHORS_NOT_FOUND = "Авторы с ID %s не найдены";

    private AuthorNotFoundException(String message) {
        super(message);
    }

    public static AuthorNotFoundException authorNotFound(Long id) {
        return new AuthorNotFoundException(String.format(AUTHOR_NOT_FOUND, id));
    }

    public static AuthorNotFoundException authorsNotFound(Collection<Long> ids) {
        return new AuthorNotFoundException(String.format(AUTHORS_NOT_FOUND, NotFoundException.joinIdCollection(ids)));
    }
}
