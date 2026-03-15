package ru.valkeru.libdemo.repository.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.model.entity.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    @Query("select u.id from User u where u.username = :username")
    Optional<UUID> getIdByUsername(String username);
}
