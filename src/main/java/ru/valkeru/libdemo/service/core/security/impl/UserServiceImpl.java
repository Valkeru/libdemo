package ru.valkeru.libdemo.service.core.security.impl;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.model.dto.security.LibraryUser;
import ru.valkeru.libdemo.model.entity.user.User;
import ru.valkeru.libdemo.repository.jpa.user.UserRepository;
import ru.valkeru.libdemo.service.core.security.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getReference(UUID id) {
        return userRepository.getReferenceById(id);
    }

    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> getUsernameNotFoundException(username));

        return new LibraryUser(user.getId(), username, user.getPassword(), user.getRole());
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
