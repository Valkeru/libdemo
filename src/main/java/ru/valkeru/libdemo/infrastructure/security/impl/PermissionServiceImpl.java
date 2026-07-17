package ru.valkeru.libdemo.infrastructure.security.impl;

import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.valkeru.libdemo.infrastructure.entity.RolePermission;
import ru.valkeru.libdemo.infrastructure.entity.RolePermissionPk;
import ru.valkeru.libdemo.infrastructure.repository.RolePermissionRepository;
import ru.valkeru.libdemo.infrastructure.security.PermissionService;
import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final RolePermissionRepository repository;

    @Override
    public void checkPatch(LibraryPrincipal principal, BookInstancePatchDto dto) {
        JsonNullable<String> inventoryNumber = dto.getInventoryNumber();
        if (inventoryNumber.isPresent()) {
            Role role = principal.role();
            if (!repository.existsByIdRoleAndIdPermission(role, Permission.BOOK_INSTANCE_INVENTORY_NUMBER_EDIT)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
    }

    @Override
    public void replacePermissions(Role role, Collection<Permission> permissions) {
        List<RolePermission> rolePermissions = permissions.stream()
            .flatMap(permission -> permission.resolvePermissions().stream())
            .distinct()
            .map(permission -> RolePermission.builder().id(new RolePermissionPk(role, permission)).build())
            .toList();

        repository.deletePermissions(role);
        repository.persistAll(rolePermissions);
    }

    @Override
    public Collection<Permission> findPermissionsByRole(Role role) {
        List<Permission> result = repository.findAllByIdRole(role).stream()
            .map(RolePermission::getPermission)
            .toList();

        result.stream()
            .filter(Permission::isComposite)
            .findFirst().ifPresent(permission -> {
                throw new IllegalStateException("Composite permission stored in DB: {%s}".formatted(permission));
            });

        return result;
    }
}
