package ru.valkeru.libdemo.model.dto.security;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.valkeru.libdemo.security.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class LibraryUser implements UserDetails {

    private final String username;

    private final String password;

    private final Role role;

    private final Set<String> permissions;

    public LibraryUser(String username, String password, Role role, Set<String> permissions) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.permissions = permissions;
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authorities = new ArrayList<>();
        authorities.add(role.name());
        authorities.addAll(permissions);

        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
    }
}
