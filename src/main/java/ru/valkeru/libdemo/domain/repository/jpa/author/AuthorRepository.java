package ru.valkeru.libdemo.domain.repository.jpa.author;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.domain.entity.Author;
import ru.valkeru.libdemo.domain.repository.jpa.author.qdsl.AuthorDslRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AuthorRepository extends BaseJpaRepository<Author, UUID>, AuthorDslRepository {

    @Transactional
    @Modifying
    @Query("delete from Author where id = :id")
    int deleteAuthorById(UUID id);

    Page<Author> findAll(Pageable pageable);

    List<Author> findByIdIn(Collection<UUID> ids);
}
