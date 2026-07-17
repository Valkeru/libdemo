package ru.valkeru.libdemo.infrastructure.security;

import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.util.Collection;

public interface PermissionService {

    void checkPatch(LibraryPrincipal principal, BookInstancePatchDto dto);

    /**
     * Set permissions for role
     * @param role Role to update
     * @param permissions New permissions.
     *                   Composite permissions are accepted and resolved into atomic permissions
     */
    void replacePermissions(Role role, Collection<Permission> permissions);

    /**
     * Returns effective atomic permissions for given role
     * Composite permissions aren't allowed in database
     */
    Collection<Permission> findPermissionsByRole(Role role);
}
