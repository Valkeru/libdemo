package ru.valkeru.libdemo.model.dto.security;

import org.springframework.security.core.GrantedAuthority;
import ru.valkeru.libdemo.security.Role;

import java.util.List;
import java.util.UUID;

public record LibraryPrincipal(UUID id, String userName, Role role, List<? extends GrantedAuthority> authorities) {
}
