package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.IntegrityViolationException;

public final class AuthorViolationException extends IntegrityViolationException {

    private static final String UNABLE_TO_REMOVE_WITH_BOOKS = "Невозможно удалить автора, имеющего книги";

    private AuthorViolationException(String message) {
        super(message);
    }

    public static AuthorViolationException unableToDeleteHasBooks() {
        return new AuthorViolationException(UNABLE_TO_REMOVE_WITH_BOOKS);
    }
}
