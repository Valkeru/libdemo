package ru.valkeru.libdemo.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum Role {

    ROLE_ADMIN("ADMIN"),
    ROLE_LIBRARIAN("LIBRARIAN"),
    ROLE_MANAGER("MANAGER"),
    ROLE_READER("READER"),
    ROLE_USER("USER");

    private final String roleName;

    public static String[] getServiceRoleNames() {
        return Stream.of(
            ROLE_ADMIN,
            ROLE_MANAGER,
            ROLE_LIBRARIAN
        )
            .map(Role::getRoleName)
            .toArray(String[]::new);
    }
}
