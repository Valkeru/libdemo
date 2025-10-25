package ru.valkeru.libdemo.repository.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.elasticsearch.base.AuthorElasticsearchRepository;
import ru.valkeru.libdemo.repository.jpa.AuthorRepository;

import java.util.UUID;

@Slf4j
@Component
public class AuthorRepositoryFacade {

    private final AuthorElasticsearchRepository authorElasticsearchRepository;
    private final AuthorRepository authorRepository;

    public AuthorRepositoryFacade(AuthorElasticsearchRepository authorElasticsearchRepository,
                                  AuthorRepository authorRepository) {
        this.authorElasticsearchRepository = authorElasticsearchRepository;
        this.authorRepository = authorRepository;
    }

    public Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        try {
            return authorElasticsearchRepository.listAllAuthors(filter, pageable);
        } catch (Exception e) {
            log.error("Elasticsearch Authors list search failed: {}", e.getMessage(), e);

            return authorRepository.listAllAuthors(filter, pageable);
        }
    }

    public AuthorDto getAuthorById(UUID id) {
        try {
            return authorElasticsearchRepository.getAuthorById(id);
        } catch (Exception e) {
            log.error("Elasticsearch Author search failed: {}", e.getMessage(), e);

            return authorRepository.getAuthorById(id);
        }
    }
}
