package ru.valkeru.libdemo.domain.exception;

import java.io.Serial;

public final class ConflictDomainException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7242594066741329303L;

    private static final String BOOK_INSTANCE_NOT_AVAILABLE = "There is no book instance available to borrow";
    private static final String LENDING_STATUS_INVALID = "Lending status is not valid for requested operation";
    private static final String CURRENT_LIBRARY_CARD_NOT_EXISTS = "There is no current library card for user";
    private static final String NOT_ACTIVE_LIBRARY_CARD = "Library card is not active";

    private ConflictDomainException(String message) {
        super(message);
    }

    public static ConflictDomainException bookInstanceNotAvailable() {
        return new ConflictDomainException(BOOK_INSTANCE_NOT_AVAILABLE);
    }

    public static ConflictDomainException notValidLendingStatus() {
        return new ConflictDomainException(LENDING_STATUS_INVALID);
    }

    public static ConflictDomainException hasNoLibraryCard() {
        return new ConflictDomainException(CURRENT_LIBRARY_CARD_NOT_EXISTS);
    }

    public static ConflictDomainException notActiveCard() {
        return new ConflictDomainException(NOT_ACTIVE_LIBRARY_CARD);
    }
}
