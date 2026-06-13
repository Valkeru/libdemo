package ru.valkeru.libdemo.service.core.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.exception.impl.AuthorNotFoundException;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.repository.jpa.author.AuthorRepository;
import ru.valkeru.libdemo.service.core.AuthorService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    @Override
    public Author createAuthor(AuthorDto authorDto) {
        Author author = new Author();
        mapper.updateAuthor(authorDto, author);

        return repository.persist(author);
    }

    @Override
    public void update(AuthorDto dto, Author author) {
        mapper.updateAuthor(dto, author);

        repository.merge(author);
    }

    @Override
    public Optional<Author> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Author> getByIdList(Set<UUID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        List<Author> authors = repository.findByIdIn(ids);
        if (authors.size() == ids.size()) {
            return authors;
        }

        Set<UUID> notFoundIds = new HashSet<>(ids);
        Set<UUID> found = authors.stream()
            .map(Author::getId)
            .collect(Collectors.toSet());

        notFoundIds.removeAll(found);

        throw AuthorNotFoundException.notFound(notFoundIds);
    }

    @Override
    public void deleteAuthorById(UUID id) {
        if (repository.deleteAuthorById(id) == 0) {
            throw AuthorNotFoundException.authorNotFound(id);
        }
    }
}
