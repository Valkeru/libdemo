package ru.valkeru.libdemo.repository.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.elasticsearch.AuthorElasticsearchRepository;
import ru.valkeru.libdemo.repository.jpa.author.AuthorRepository;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorRepositoryFacade {

    private final AuthorElasticsearchRepository authorElasticsearchRepository;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        try {
            return authorElasticsearchRepository.listAllAuthors(filter, pageable);
        } catch (Exception e) {
            log.error("Elasticsearch Authors list search failed: {}", e.getMessage(), e);

            return authorRepository.listAllAuthors(filter, pageable).map(authorMapper::toDto);
        }
    }

    public AuthorDto getAuthorById(UUID id) {
        try {
            return authorElasticsearchRepository.getAuthorById(id);
        } catch (Exception e) {
            log.error("Elasticsearch Author search failed: {}", e.getMessage(), e);

            return authorMapper.toDto(authorRepository.getAuthorById(id));
        }
    }
}
