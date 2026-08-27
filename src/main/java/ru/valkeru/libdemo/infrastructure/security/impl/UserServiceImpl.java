package ru.valkeru.libdemo.infrastructure.security.impl;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.domain.exception.NotFoundDomainException;
import ru.valkeru.libdemo.domain.repository.jpa.user.UserRepository;
import ru.valkeru.libdemo.infrastructure.security.UserService;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.security.Role;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public void createUser(SignUpRequest request, Role role) {
        User user = User.builder()
            .username(request.getUsername())
            .password(hashPassword(request.getPassword()))
            .role(role)
            .build();

        repository.persist(user);
    }

    @Override
    public User getReference(UUID id) {
        return repository.getReferenceById(id);
    }

    @Override
    public User getById(UUID id) {
        return repository.findById(id)
            .orElseThrow(NotFoundDomainException::user);
    }

    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }

    @Override
    public void updateRole(User user, Role role) {
        user.setRole(role);
        repository.merge(user);
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
    public Collection<User> getReadersWithoutLibraryCards() {
        return List.of();
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
