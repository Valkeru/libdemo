package ru.valkeru.libdemo.repository.jpa.author.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

public interface AuthorDslRepository {

    Page<Author> listAllAuthors(AuthorFilter filter, Pageable pageable);
}
