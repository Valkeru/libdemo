package ru.valkeru.libdemo.repository.jpa.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.repository.jpa.author.qdsl.AuthorDslRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID>, AuthorDslRepository {

    Author getAuthorById(UUID id);

    @Transactional
    @Modifying
    @Query("delete from Author where id = :id")
    int deleteAuthorById(UUID id);
}
