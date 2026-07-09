package ru.valkeru.libdemo.security;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Permission {

    AUTHOR_CREATE,
    AUTHOR_UPDATE,
    AUTHOR_DELETE,
    AUTHOR_FULL(
        AUTHOR_CREATE,
        AUTHOR_UPDATE,
        AUTHOR_DELETE
    ),

    CYCLE_CREATE,
    CYCLE_UPDATE,
    CYCLE_DELETE,
    CYCLE_FULL(
        CYCLE_CREATE,
        CYCLE_UPDATE,
        CYCLE_DELETE
    ),

    SERIES_CREATE,
    SERIES_UPDATE,
    SERIES_DELETE,
    SERIES_FULL(
        SERIES_CREATE,
        SERIES_UPDATE,
        SERIES_DELETE
    ),

    BOOK_CREATE,
    BOOK_UPDATE,
    BOOK_DELETE,
    BOOK_FULL(
        BOOK_CREATE,
        BOOK_UPDATE,
        BOOK_DELETE
    ),

    BOOK_INSTANCE_CREATE,
    BOOK_INSTANCE_UPDATE,
    BOOK_INSTANCE_DELETE,

    SERVICE_INDEXING,
    SERVICE_LIBRARY_CARD_CREATE,
    SERVICE_LIBRARY_CARD_VIEW;

    private final Set<Permission> included;

    Permission(Permission ...included) {
        this.included = Set.of(included);
    }

    public Set<Permission> expand() {
        if (included != null && !included.isEmpty()) {
            return included;
        }

        return Set.of(this);
    }
}
