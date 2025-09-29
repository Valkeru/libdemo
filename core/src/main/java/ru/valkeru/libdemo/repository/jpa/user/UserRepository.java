package ru.valkeru.libdemo.repository.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.valkeru.libdemo.model.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
