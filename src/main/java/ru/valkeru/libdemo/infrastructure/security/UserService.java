package ru.valkeru.libdemo.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.security.Role;

import java.util.Collection;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    void createUser(SignUpRequest request, Role role);

    User getReference(UUID id);

    User getById(UUID id);

    void updateRole(User user, Role role);

    boolean isValidPassword(UserDetails user, String providedPassword);

    /**
     * Returns users with role READER but having no active library cards. This method is needed for following role demotion
     */
    Collection<User> getReadersWithoutLibraryCards();
}
