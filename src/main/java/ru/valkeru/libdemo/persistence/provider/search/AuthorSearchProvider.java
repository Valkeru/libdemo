package ru.valkeru.libdemo.persistence.provider.search;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.AuthorDto;

public interface AuthorSearchProvider {

    Page<AuthorDto> listAllAuthors(String[] tokens, Pageable pageable);
}
