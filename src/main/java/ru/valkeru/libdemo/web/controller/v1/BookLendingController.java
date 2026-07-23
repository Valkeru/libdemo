package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.application.BookLendingApplicationService;
import ru.valkeru.libdemo.model.dto.lending.BookLendingDto;
import ru.valkeru.libdemo.model.dto.lending.BookLendingListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.web.api.v1.BookLendingApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookLendingController implements BookLendingApi {

    private final BookLendingApplicationService service;

    @Override
    public ResponseEntity<Void> reserveBook(UUID bookId, LibraryPrincipal principal) {
        UUID lendingId = service.createLending(bookId, principal);

        return ResponseEntity.created(
                MvcUriComponentsBuilder.fromMethodCall(
                    MvcUriComponentsBuilder.on(BookLendingApi.class).getLending(lendingId, principal)
                ).build().toUri()
            )
            .build();
    }

    @Override
    public ResponseEntity<Void> cancelReservation(UUID id, LibraryPrincipal principal) {
        service.cancelLending(id, principal);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<BookLendingListDto>> getLendingsList(Pageable pageable, LibraryPrincipal principal) {
        Page<BookLendingListDto> lendings = service.listLendings(principal, pageable);

        return ResponseEntity.ok(lendings);
    }

    @Override
    public ResponseEntity<BookLendingDto> getLending(UUID id, LibraryPrincipal principal) {
        BookLendingDto lending = service.getLending(id, principal);

        return ResponseEntity.ok(lending);
    }
}
