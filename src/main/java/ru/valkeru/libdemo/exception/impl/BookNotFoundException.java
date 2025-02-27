package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.NotFoundException;

public final class BookNotFoundException extends NotFoundException {

    private static final String BOOK_NOT_FOUND = "Книга %d не найдена";

    private BookNotFoundException(String message) {
        super(message);
    }

    public static BookNotFoundException bookNotFound(Long id) {
        return new BookNotFoundException(String.format(BOOK_NOT_FOUND, id));
    }
}
