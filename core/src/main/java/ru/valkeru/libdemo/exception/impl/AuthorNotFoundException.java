package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.NotFoundException;

import java.util.UUID;

public final class AuthorNotFoundException extends NotFoundException {

    private static final String AUTHOR_NOT_FOUND = "Автор с ID %s не найден";

    private AuthorNotFoundException(String message) {
        super(message);
    }

    public static AuthorNotFoundException authorNotFound(UUID id) {
        return new AuthorNotFoundException(String.format(AUTHOR_NOT_FOUND, id));
    }
}
