package ru.valkeru.libdemo.service.core;

import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AuthorService {

    Author createAuthor(AuthorDto authorDto);

    Optional<Author> findById(UUID id);

    List<Author> getByIdList(Set<UUID> ids);

    void deleteAuthorById(UUID id);

    void update(AuthorDto dto, Author author);
}
