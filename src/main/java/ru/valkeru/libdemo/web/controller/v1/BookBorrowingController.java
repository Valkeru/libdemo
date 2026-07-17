package ru.valkeru.libdemo.web.controller.v1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingDto;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.web.api.v1.BookBorrowingApi;

import java.util.UUID;

@RestController
public class BookBorrowingController implements BookBorrowingApi {

    @Override
    public ResponseEntity<Void> reserveBook(UUID bookId, LibraryPrincipal principal) {
        return null;
    }

    @Override
    public ResponseEntity<Void> cancelReservation(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<Page<BookBorrowingListDto>> getBorrowingsList(Pageable pageable, LibraryPrincipal principal) {
        return null;
    }

    @Override
    public ResponseEntity<BookBorrowingDto> getBorrowing(UUID id, LibraryPrincipal principal) {
        return null;
    }
}
