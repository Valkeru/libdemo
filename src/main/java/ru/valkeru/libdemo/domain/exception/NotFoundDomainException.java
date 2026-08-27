package ru.valkeru.libdemo.domain.exception;

import java.io.Serial;

public final class NotFoundDomainException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -232283881685445924L;

    private static final String BOOK_INSTANCE_NOT_FOUND = "Book instance not found";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String LIBRARY_CARD_NOT_FOUND = "Library card not found";
    private static final String LENDING_NOT_FOUND = "Lending is not found";

    private NotFoundDomainException(String message) {
        super(message);
    }

    public static NotFoundDomainException bookInstance() {
        return new NotFoundDomainException(BOOK_INSTANCE_NOT_FOUND);
    }

    public static NotFoundDomainException user() {
        return new NotFoundDomainException(USER_NOT_FOUND);
    }

    public static NotFoundDomainException libraryCard() {
        return new NotFoundDomainException(LIBRARY_CARD_NOT_FOUND);
    }

    public static NotFoundDomainException lending() {
        return new NotFoundDomainException(LENDING_NOT_FOUND);
    }
}
