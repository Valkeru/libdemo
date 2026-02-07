package ru.valkeru.libdemo.service.core.security.impl;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.repository.jpa.user.UserRepository;
import ru.valkeru.libdemo.service.core.security.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
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
}
