package ru.valkeru.libdemo.service.core.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.valkeru.libdemo.model.entity.user.User;

public interface UserService extends UserDetailsService {

    User getReferenceByUsername(String username);

    boolean isValidPassword(UserDetails user, String providedPassword);

    String hashPassword(String password);
}
