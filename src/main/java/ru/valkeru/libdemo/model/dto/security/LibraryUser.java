package ru.valkeru.libdemo.model.dto.security;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.valkeru.libdemo.security.Role;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class LibraryUser implements UserDetails {

    private final UUID id;

    private final String username;

    private final String password;

    private final Role role;

    public LibraryUser(UUID id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
