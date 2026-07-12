package ru.valkeru.libdemo.infrastructure.security;

import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

public interface PermissionService {

    void checkPatch(LibraryPrincipal principal, BookInstancePatchDto dto);
}
