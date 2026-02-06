package ru.valkeru.libdemo.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

import java.util.UUID;

public interface AuthorElasticsearchCustomRepository {

    Page<AuthorDocument> listAllAuthors(AuthorFilter filter, Pageable pageable);

    AuthorDocument getAuthorById(UUID id);
}
