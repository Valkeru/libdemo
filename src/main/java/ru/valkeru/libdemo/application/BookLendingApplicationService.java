package ru.valkeru.libdemo.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.lending.BookLendingDto;
import ru.valkeru.libdemo.model.dto.lending.BookLendingListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.request.lending.ServiceLendingFilter;

import java.util.UUID;

public interface BookLendingApplicationService {

    /**
     * Create a lending in "RESERVED" state by user.
     * User should reserve a book first and then borrow it from a library
     *
     * @return Created reservation ID
     */
    UUID createLending(UUID bookId, LibraryPrincipal principal);

    /**
     * Create a lending at place (create, get a reserve info, next issue a book instance was reserved)
     *
     * @return Created reservation ID
     */
    UUID createLending(UUID bookId, UUID libraryCardId);

    BookLendingDto getLending(UUID id);

    BookLendingDto getLending(UUID id, LibraryPrincipal principal);

    /**
     * Issue a previously reserved book
     */
    void issueReservedBook(UUID reservedLendingId);

    /**
     * Cancel reserved lending by staff
     */
    void cancelLending(UUID id);

    /**
     * Cancel reserved lending by user
     */
    void cancelLending(UUID id, LibraryPrincipal principal);

    Page<BookLendingListDto> listLendings(LibraryPrincipal principal, Pageable pageable);

    void returnBook(UUID lendingId);

    default Page<BookLendingListDto> listLendings(ServiceLendingFilter filter, Pageable pageable) {
        return Page.empty(pageable);
    }
}
