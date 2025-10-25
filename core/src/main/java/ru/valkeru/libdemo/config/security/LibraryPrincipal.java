package ru.valkeru.libdemo.config.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LibraryPrincipal {

    private String userName;

    private String role;
}
