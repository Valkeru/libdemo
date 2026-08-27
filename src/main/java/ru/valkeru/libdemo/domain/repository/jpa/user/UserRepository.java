package ru.valkeru.libdemo.domain.repository.jpa.user;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.domain.entity.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseJpaRepository<User, UUID> {

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(String username);
}
