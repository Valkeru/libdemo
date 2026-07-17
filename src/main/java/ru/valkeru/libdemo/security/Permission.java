package ru.valkeru.libdemo.security;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Role permissions<br/>
 * Permission may be composite and serve as alias for a set of effective permissions<br/>
 * Use {@link #resolvePermissions()} to get the effective permission set
 */
public enum Permission {

    AUTHOR_CREATE,
    AUTHOR_UPDATE,
    AUTHOR_DELETE,
    AUTHOR_CRUD(
        AUTHOR_CREATE,
        AUTHOR_UPDATE,
        AUTHOR_DELETE
    ),

    CYCLE_CREATE,
    CYCLE_UPDATE,
    CYCLE_DELETE,
    CYCLE_CRUD(
        CYCLE_CREATE,
        CYCLE_UPDATE,
        CYCLE_DELETE
    ),

    SERIES_CREATE,
    SERIES_UPDATE,
    SERIES_DELETE,
    SERIES_CRUD(
        SERIES_CREATE,
        SERIES_UPDATE,
        SERIES_DELETE
    ),

    BOOK_CREATE,
    BOOK_UPDATE,
    BOOK_DELETE,
    BOOK_CRUD(
        BOOK_CREATE,
        BOOK_UPDATE,
        BOOK_DELETE
    ),

    BOOK_INSTANCE_CREATE,
    BOOK_INSTANCE_UPDATE,
    BOOK_INSTANCE_VIEW,
    BOOK_INSTANCE_DELETE,
    BOOK_INSTANCE_INVENTORY_NUMBER_EDIT,
    BOOK_INSTANCE_CRUD(
        BOOK_INSTANCE_CREATE,
        BOOK_INSTANCE_UPDATE,
        BOOK_INSTANCE_VIEW,
        BOOK_INSTANCE_DELETE
    ),

    SERVICE_INDEXING,
    SERVICE_LIBRARY_CARD_CREATE,
    SERVICE_LIBRARY_CARD_VIEW;

    private final Set<Permission> includedPermissions;

    Permission(Permission ...includedPermissions) {
        this.includedPermissions = Set.of(includedPermissions);
    }

    public boolean isComposite() {
        return !includedPermissions.isEmpty();
    }

    public boolean isAtomic() {
        return includedPermissions.isEmpty();
    }

    public Set<Permission> resolvePermissions() {
        return isAtomic()
            ? Set.of(this)
            : includedPermissions.stream()
              .flatMap(permission -> permission.resolvePermissions().stream())
              .collect(Collectors.toUnmodifiableSet());
    }
}
