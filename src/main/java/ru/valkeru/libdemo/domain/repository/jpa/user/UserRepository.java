package ru.valkeru.libdemo.domain.repository.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.model.dto.security.LibraryUser;
import ru.valkeru.libdemo.domain.entity.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("""
        select
            u.id as id,
            u.username as username,
            u.password as password,
            u.role as role
        from User u
        where u.username = :username
    """)
    Optional<LibraryUser> findByUsername(String username);
}
