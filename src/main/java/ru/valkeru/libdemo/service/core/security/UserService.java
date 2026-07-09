package ru.valkeru.libdemo.service.core.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.valkeru.libdemo.domain.entity.user.User;

import java.util.UUID;

public interface UserService extends UserDetailsService {

    User getReference(UUID id);

    boolean isValidPassword(UserDetails user, String providedPassword);

    String hashPassword(String password);
}
