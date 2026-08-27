package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.application.BookLendingApplicationService;
import ru.valkeru.libdemo.model.dto.lending.BookLendingDto;
import ru.valkeru.libdemo.web.api.service.BookLendingServiceApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class BookLendingServiceController implements BookLendingServiceApi {

    private final BookLendingApplicationService service;

    @Override
    public ResponseEntity<Void> reserveBook(UUID bookId, UUID libraryCardId) {
        UUID reserveId = service.createLending(bookId, libraryCardId);

        return ResponseEntity.created(
            MvcUriComponentsBuilder.fromMethodCall(
                on(BookLendingServiceApi.class).getLending(reserveId)
            ).build().toUri()
        ).build();
    }

    @Override
    public ResponseEntity<BookLendingDto> getLending(UUID id) {
        BookLendingDto dto = service.getLending(id);

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<Void> issueReservedBook(UUID id) {
        service.issueReservedBook(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> cancelReserve(UUID id) {
        service.cancelLending(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> returnBook(UUID lendingId) {
        service.returnBook(lendingId);

        return ResponseEntity.noContent().build();
    }
}
