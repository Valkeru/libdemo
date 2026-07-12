package ru.valkeru.libdemo.domain.service;

import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.BookInstance;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceViewDto;

import java.util.UUID;

public interface BookInstanceService {

    BookInstance createInstance(BookInstanceCreateDto dto, Book book);

    BookInstance getBookInstance(UUID id);

    void updateInstance(UUID id, BookInstanceViewDto dto);
}
