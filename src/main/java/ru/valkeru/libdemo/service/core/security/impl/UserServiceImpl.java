package ru.valkeru.libdemo.service.core.security.impl;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.config.security.RolePermissionProperties;
import ru.valkeru.libdemo.model.dto.security.LibraryUser;
import ru.valkeru.libdemo.model.entity.user.User;
import ru.valkeru.libdemo.repository.jpa.user.UserRepository;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;
import ru.valkeru.libdemo.service.core.security.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RolePermissionProperties permissionProperties;
    private final UserRepository userRepository;

    @Override
    public User getReferenceByUsername(String username) {
        UUID id = userRepository.getIdByUsername(username)
            .orElseThrow(() -> getUsernameNotFoundException(username));

        return userRepository.getReferenceById(id);
    }

    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> getUsernameNotFoundException(username));

        Map<Role, Collection<Permission>> rolePermissionsMap = permissionProperties.getPermissions();
        Role role = user.getRole();
        Set<String> permissions = rolePermissionsMap.getOrDefault(role, List.of())
            .stream()
            .map(Permission::name)
            .collect(Collectors.toSet());

        return new LibraryUser(username, user.getPassword(), role, permissions);
    }

    @Override
    public boolean isValidPassword(UserDetails user, String providedPassword) {
        String hashedPassword = user.getPassword();
        if (hashedPassword == null) {
            return false;
        }

        return BCrypt.checkpw(providedPassword, hashedPassword);
    }

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private UsernameNotFoundException getUsernameNotFoundException(String username) {
        return new UsernameNotFoundException("User %s not found".formatted(username));
    }
}
