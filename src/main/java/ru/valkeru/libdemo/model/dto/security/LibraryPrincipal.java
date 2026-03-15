package ru.valkeru.libdemo.model.dto.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record LibraryPrincipal(String userName, List<? extends GrantedAuthority> authorities) {
}
