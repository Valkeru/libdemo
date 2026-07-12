package ru.valkeru.libdemo.infrastructure.security.impl;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.valkeru.libdemo.infrastructure.security.PermissionService;
import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.security.PatchPermission;
import ru.valkeru.libdemo.security.Role;

import java.util.List;
import java.util.Map;

@Component
public class PermissionServiceImpl implements PermissionService {

    private final Map<Role, List<PatchPermission>> patchPermissions = Map.of(
        Role.ROLE_MANAGER, List.of(PatchPermission.BOOK_INSTANCE_INVENTORY_NUMBER_UPDATE)
    );

    @Override
    public void checkPatch(LibraryPrincipal principal, BookInstancePatchDto dto) {
        JsonNullable<String> inventoryNumber = dto.getInventoryNumber();
        if (inventoryNumber.isPresent()) {
            Role role = principal.role();
            List<PatchPermission> permissions = patchPermissions.getOrDefault(role, List.of());

            if (!permissions.contains(PatchPermission.BOOK_INSTANCE_INVENTORY_NUMBER_UPDATE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
    }
}
