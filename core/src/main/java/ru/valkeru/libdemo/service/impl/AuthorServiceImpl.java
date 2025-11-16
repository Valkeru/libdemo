package ru.valkeru.libdemo.service.impl;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.AuthorNotFoundException;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.elasticsearch.AuthorElasticsearchRepository;
import ru.valkeru.libdemo.repository.facade.AuthorRepositoryFacade;
import ru.valkeru.libdemo.repository.jpa.author.AuthorRepository;
import ru.valkeru.libdemo.service.base.AuthorService;
import ru.valkeru.libdemo.util.ElasticsearchUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final AuthorElasticsearchRepository authorElasticsearchRepository;
    private final AuthorRepositoryFacade authorRepositoryFacade;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper,
                             AuthorElasticsearchRepository authorElasticsearchRepository,
                             AuthorRepositoryFacade authorRepositoryFacade) {

        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.authorElasticsearchRepository = authorElasticsearchRepository;
        this.authorRepositoryFacade = authorRepositoryFacade;
    }

    @Transactional
    @Override
    public Author createOrUpdateAuthor(AuthorDto authorDto) {
        Author author = getAuthorEntity(authorDto);
        authorMapper.updateAuthor(authorDto, author);

        return authorRepository.save(author);
    }

    @Override
    public Page<Author> getAuthors(AuthorFilter filter, Pageable pageable) {
        return authorRepository.listAllAuthors(filter, pageable);
    }

    @Nonnull
    @Override
    public Author getAuthorById(UUID id) {
        return Optional.ofNullable(authorRepository.getAuthorById(id))
                .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id));
    }

    @Transactional
    @Override
    public void deleteAuthorById(UUID id) {
        if (authorRepository.deleteAuthorById(id) == 0) {
            throw AuthorNotFoundException.authorNotFound(id);
        }
    }

    @Override
    public void reindexAuthors() {
        ElasticsearchUtil.createOrUpdateElasticsearchIndex(AuthorDocument.class);

        List<AuthorDocument> authorDocuments = authorRepository.findAll().stream()
                .map(authorMapper::toDocument)
                .toList();

        authorElasticsearchRepository.deleteAll();
        authorElasticsearchRepository.saveAll(authorDocuments);
    }

    private Author getAuthorEntity(AuthorDto authorDto) {
        return Optional.ofNullable(authorDto.getId())
                .map(this::getAuthorEntity)
                .orElse(new Author());
    }

    private Author getAuthorEntity(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id));
    }
}
