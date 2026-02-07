package ru.valkeru.libdemo.service.core.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean isValidPassword(UserDetails user, String providedPassword);

    String hashPassword(String password);
}
