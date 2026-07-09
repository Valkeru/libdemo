package ru.valkeru.libdemo.infrastructure.provider.search;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.AuthorDto;

public interface AuthorSearchProvider {

    Page<AuthorDto> listAllAuthors(String[] tokens, Pageable pageable);

    default void reindex() {
        // Default empty implementation
    }
}
