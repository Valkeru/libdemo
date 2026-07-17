package ru.valkeru.libdemo.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingDto;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

public interface BookBorrowingApplicationService {

    UUID createBorrowing(UUID bookId, LibraryPrincipal principal);

    BookBorrowingDto getBorrowing(UUID id, LibraryPrincipal principal);

    void cancelBorrowing(UUID id, LibraryPrincipal principal);

    Page<BookBorrowingListDto> listBorrowing(LibraryPrincipal principal, Pageable pageable);
}
