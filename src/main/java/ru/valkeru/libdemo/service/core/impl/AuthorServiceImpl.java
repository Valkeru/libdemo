package ru.valkeru.libdemo.service.core.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.exception.impl.AuthorNotFoundException;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.repository.jpa.author.AuthorRepository;
import ru.valkeru.libdemo.service.core.AuthorService;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    public Author createOrUpdateAuthor(AuthorDto authorDto) {
        Author author = getAuthorEntity(authorDto);
        authorMapper.updateAuthor(authorDto, author);

        return author.getId() == null
                ? authorRepository.persist(author)
                : authorRepository.merge(author);
    }

    @Override
    public Author getAuthorById(UUID id) {
        return Optional.ofNullable(authorRepository.getAuthorById(id))
                .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id));
    }

    @Override
    public void deleteAuthorById(UUID id) {
        if (authorRepository.deleteAuthorById(id) == 0) {
            throw AuthorNotFoundException.authorNotFound(id);
        }
    }

    private Author getAuthorEntity(AuthorDto authorDto) {
        UUID id = authorDto.getId();

        return id != null
                ? authorRepository.findById(id)
                    .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id))
                : new Author();
    }
}
