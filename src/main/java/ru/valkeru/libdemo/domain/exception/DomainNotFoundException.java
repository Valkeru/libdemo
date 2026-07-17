package ru.valkeru.libdemo.domain.exception;

import java.io.Serial;

public final class DomainNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6448436209852518880L;

    private static final String BOOK_INSTANCE_NOT_FOUND = "Book instance not found";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String LIBRARY_CARD_NOT_FOUND = "User not found";

    private DomainNotFoundException(String message) {
        super(message);
    }

    public static DomainNotFoundException bookInstance() {
        return new DomainNotFoundException(BOOK_INSTANCE_NOT_FOUND);
    }

    public static DomainNotFoundException user() {
        return new DomainNotFoundException(USER_NOT_FOUND);
    }

    public static DomainNotFoundException libraryCard() {
        return new DomainNotFoundException(LIBRARY_CARD_NOT_FOUND);
    }
}
