package ru.valkeru.libdemo.repository.jpa.author;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.repository.jpa.author.qdsl.AuthorDslRepository;

import java.util.Collection;
import java.util.UUID;

public interface AuthorRepository extends BaseJpaRepository<Author, UUID>, AuthorDslRepository {

    Author getAuthorById(UUID id);

    @Transactional
    @Modifying
    @Query("delete from Author where id = :id")
    int deleteAuthorById(UUID id);

    Collection<Author> findAll(Pageable pageable);
}
