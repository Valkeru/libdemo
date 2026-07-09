package ru.valkeru.libdemo.domain.repository.jpa.author.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.domain.entity.Author;

public interface AuthorDslRepository {

    Page<Author> listAllAuthors(String[] tokens, Pageable pageable);
}
