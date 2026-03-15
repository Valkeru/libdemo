package ru.valkeru.libdemo.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {

    ROLE_ADMIN("ADMIN"),
    ROLE_LIBRARIAN("LIBRARIAN"),
    ROLE_USER("USER");

    private final String roleName;
}
