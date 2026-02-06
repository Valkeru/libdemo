package ru.valkeru.libdemo.service.core;

import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;

import java.util.UUID;

public interface AuthorService {

    Author createOrUpdateAuthor(AuthorDto authorDto);

    Author getAuthorById(UUID id);

    void deleteAuthorById(UUID id);
}
