package ru.valkeru.libdemo.persistence.repository.jpa.author.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.persistence.entity.Author;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AuthorDslRepository {

    Page<Author> listAllAuthors(String[] tokens, Pageable pageable);

    List<Author> getAuthorSlice(Instant createdAt, UUID id, int limit);
}
