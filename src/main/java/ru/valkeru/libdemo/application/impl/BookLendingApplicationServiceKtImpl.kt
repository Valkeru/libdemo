package ru.valkeru.libdemo.application.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.valkeru.libdemo.application.BookLendingApplicationService
import ru.valkeru.libdemo.domain.entity.Book
import ru.valkeru.libdemo.domain.entity.BookInstance
import ru.valkeru.libdemo.domain.entity.BookLending
import ru.valkeru.libdemo.domain.entity.LibraryCard
import ru.valkeru.libdemo.domain.enums.LendingStatus
import ru.valkeru.libdemo.domain.service.BookInstanceService
import ru.valkeru.libdemo.domain.service.BookLendingService
import ru.valkeru.libdemo.domain.service.BookService
import ru.valkeru.libdemo.domain.service.LibraryCardService
import ru.valkeru.libdemo.infrastructure.security.UserService
import ru.valkeru.libdemo.mapper.BookLendingMapper
import ru.valkeru.libdemo.model.dto.internal.BookLendingCreateRequest
import ru.valkeru.libdemo.model.dto.lending.BookLendingDto
import ru.valkeru.libdemo.model.dto.lending.BookLendingListDto
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal
import ru.valkeru.libdemo.model.request.lending.ServiceLendingFilter
import java.time.Instant
import java.time.Period
import java.time.ZoneOffset
import java.util.UUID


@Component
@Primary
@Transactional(readOnly = true)
class BookLendingApplicationServiceKtImpl(
    private val baseService: BookLendingApplicationServiceImpl,
    private val userService: UserService,
    private val bookService: BookService,
    private val bookInstanceService: BookInstanceService,
    private val libraryCardService: LibraryCardService,
    private val bookLendingService: BookLendingService,
    private val mapper: BookLendingMapper,
    @Value("P\${app.policy.reservation-period-days}D")
    private val reservationPeriod: Period,
    @Value("P\${app.policy.lending-period-days}D")
    private val lendingPeriod: Period
) : BookLendingApplicationService {

    @Transactional
    override fun createLending(bookId: UUID, principal: LibraryPrincipal): UUID {
        val user = userService.getReference(principal.id())

        val libraryCard = libraryCardService.requireCurrentLibraryCard(user)
        val lending = createReservedLending(bookId, libraryCard)

        return lending.id

    }

    @Transactional
    override fun createLending(bookId: UUID, libraryCardId: UUID): UUID {
        val libraryCard = libraryCardService.getActiveLibraryCard(libraryCardId)
        val lending = createReservedLending(bookId, libraryCard)

        return lending.id
    }

    override fun getLending(id: UUID): BookLendingDto {
        return baseService.getLending(id)
    }

    override fun getLending(id: UUID, principal: LibraryPrincipal): BookLendingDto {
        return baseService.getLending(id, principal)
    }

    @Transactional
    override fun issueReservedBook(reservedLendingId: UUID) {
        baseService.issueReservedBook(reservedLendingId)
    }

    @Transactional
    override fun cancelLending(id: UUID) {
        baseService.cancelLending(id)
    }

    @Transactional
    override fun cancelLending(id: UUID, principal: LibraryPrincipal) {
        baseService.cancelLending(id, principal)
    }

    override fun listLendings(principal: LibraryPrincipal, pageable: Pageable): Page<BookLendingListDto> {
        return baseService.listLendings(principal, pageable)
    }

    @Transactional
    override fun returnBook(lendingId: UUID) {
        baseService.returnBook(lendingId)
    }

    override fun listLendings(filter: ServiceLendingFilter, pageable: Pageable): Page<BookLendingListDto> {
        return Page.empty(pageable)
    }


    private fun createReservedLending(bookId: UUID, libraryCard: LibraryCard): BookLending {
        val book: Book = bookService.getBookById(bookId)
        val bookInstance: BookInstance = bookInstanceService.getAvailableInstance(book)

        val reservedAt = Instant.now()
        val reserveDueDate = reservedAt.atZone(ZoneOffset.UTC)
            .toLocalDate()
            .plus(reservationPeriod)

        val lendingCreateRequest = BookLendingCreateRequest.builder()
            .bookInstance(bookInstance)
            .libraryCard(libraryCard)
            .reservedAt(reservedAt)
            .reservationDueDate(reserveDueDate)
            .status(LendingStatus.RESERVED)
            .build()

        return bookLendingService.createLending(lendingCreateRequest)
    }
}
