package ru.valkeru.libdemo.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.application.BookBorrowingApplicationService;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingDto;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

@Component
public class BookBorrowingApplicationServiceImpl implements BookBorrowingApplicationService {

    @Override
    public UUID createBorrowing(UUID bookId, LibraryPrincipal principal) {
        return null;
    }

    @Override
    public BookBorrowingDto getBorrowing(UUID id, LibraryPrincipal principal) {
        return null;
    }

    @Override
    public void cancelBorrowing(UUID id, LibraryPrincipal principal) {

    }

    @Override
    public Page<BookBorrowingListDto> listBorrowing(LibraryPrincipal principal, Pageable pageable) {
        return null;
    }
}
