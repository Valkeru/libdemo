package ru.valkeru.libdemo.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.valkeru.libdemo.persistence.entity.user.User;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.security.Role;

import java.util.UUID;

public interface UserService extends UserDetailsService {

    void createUser(SignUpRequest request, Role role);

    User getReference(UUID id);

    User getById(UUID id);

    void updateRole(User user, Role role);

    boolean isValidPassword(UserDetails user, String providedPassword);
}
