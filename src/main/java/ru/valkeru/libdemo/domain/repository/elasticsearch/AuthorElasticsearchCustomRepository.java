package ru.valkeru.libdemo.domain.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.domain.document.AuthorDocument;

import java.util.UUID;

public interface AuthorElasticsearchCustomRepository {

    Page<AuthorDocument> listAllAuthors(String[] tokens, Pageable pageable);

    AuthorDocument getAuthorById(UUID id);
}
