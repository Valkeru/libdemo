package ru.valkeru.libdemo.persistence.repository.jpa.user;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.model.dto.security.LibraryUser;
import ru.valkeru.libdemo.persistence.entity.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseJpaRepository<User, UUID> {

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
