package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.application.BookBorrowingApplicationService;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingDto;
import ru.valkeru.libdemo.model.dto.borrowing.BookBorrowingListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.web.api.v1.BookBorrowingApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookBorrowingController implements BookBorrowingApi {

    private final BookBorrowingApplicationService service;

    @Override
    public ResponseEntity<Void> reserveBook(UUID bookId, LibraryPrincipal principal) {
        UUID borrowingId = service.createBorrowing(bookId, principal);

        return ResponseEntity.created(
                MvcUriComponentsBuilder.fromMethodCall(
                    MvcUriComponentsBuilder.on(BookBorrowingApi.class).getBorrowing(borrowingId, principal)
                ).build().toUri()
            )
            .build();
    }

    @Override
    public ResponseEntity<Void> cancelReservation(UUID id, LibraryPrincipal principal) {
        service.cancelBorrowing(id, principal);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<BookBorrowingListDto>> getBorrowingsList(Pageable pageable, LibraryPrincipal principal) {
        Page<BookBorrowingListDto> listedBorrowing = service.listBorrowing(principal, pageable);

        return ResponseEntity.ok(listedBorrowing);
    }

    @Override
    public ResponseEntity<BookBorrowingDto> getBorrowing(UUID id, LibraryPrincipal principal) {
        BookBorrowingDto borrowing = service.getBorrowing(id, principal);

        return ResponseEntity.ok(borrowing);
    }
}
