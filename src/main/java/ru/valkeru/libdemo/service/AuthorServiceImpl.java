package ru.valkeru.libdemo.service;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.AuthorNotFoundException;
import ru.valkeru.libdemo.exception.impl.AuthorViolationException;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.elasticsearch.AuthorElasticsearchRepository;
import ru.valkeru.libdemo.repository.jpa.AuthorRepository;
import ru.valkeru.libdemo.util.ElasticsearchUtil;
import ru.valkeru.libdemo.util.SqlUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    AuthorMapper authorMapper;
    AuthorElasticsearchRepository authorElasticsearchRepository;

    @Transactional
    @Override
    public AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        Author author = getAuthorEntity(authorDto);
        authorMapper.updateAuthor(authorDto, author);

        return authorMapper.toDto(authorRepository.save(author));
    }

    @Override
    public Collection<AuthorDto> getAuthors(AuthorFilter filter, Pageable pageable) {
//        return authorRepository.getAuthors(SqlUtil.sortByCreatedAtAsc());

        return authorRepository.listAllAuthors(filter, pageable);
    }

    @Override
    public Collection<Long> checkNotExistedIds(Collection<Long> testedIds) {
        return authorRepository.getIdNotExisted(testedIds);
    }

    @Nonnull
    @Override
    public Collection<Author> getAllById(Collection<Long> ids) {
        return authorRepository.getAuthorsByIdIn(ids, SqlUtil.sortByIdAsc());
    }

    @Nonnull
    @Override
    public AuthorDto getAuthorById(Long id) {
        return Optional.ofNullable(authorRepository.getAuthorById(id))
                .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id));
    }

    @Transactional
    @Override
    public void deleteAuthorById(Long id) {
        if (authorHasBooks(id)) {
            throw AuthorViolationException.unableToDeleteHasBooks();
        }

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

    private Author getAuthorEntity(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id));
    }

    private boolean authorHasBooks(Long authorId) {
        return authorRepository.countBooksByAuthorId(authorId) > 0;
    }
}
