package ru.valkeru.libdemo.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum Role {

    ADMIN,
    LIBRARIAN,
    MANAGER,
    READER,
    USER;

    public static String[] getServiceRoleNames() {
        return Stream.of(
            ADMIN,
            MANAGER,
            LIBRARIAN
        )
            .map(Role::name)
            .toArray(String[]::new);
    }

    public String authorityName() {
        return "ROLE_" + name();
    }
}
