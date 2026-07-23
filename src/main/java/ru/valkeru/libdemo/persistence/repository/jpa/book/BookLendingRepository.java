package ru.valkeru.libdemo.persistence.repository.jpa.book;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.persistence.entity.BookLending;
import ru.valkeru.libdemo.persistence.entity.user.User;
import ru.valkeru.libdemo.persistence.enums.LendingStatus;
import ru.valkeru.libdemo.persistence.projection.BookLendingListProjection;
import ru.valkeru.libdemo.persistence.projection.BookLendingProjection;

import java.util.Optional;
import java.util.UUID;

public interface BookLendingRepository extends BaseJpaRepository<BookLending, UUID> {

    @Query("""
            select b.id as id,
                   bi.book.id as bookId,
                   b.status as status
                from BookLending b
                inner join b.bookInstance bi
                inner join b.readersCard rc
            where rc.user = :user
        """)
    Page<BookLendingListProjection> getListByUser(User user, Pageable pageable);

    @Query("""
            select b.id as id,
                   bi.book.id as bookId,
                   b.reservedAt as reservedAt,
                   b.borrowedAt as borrowedAt,
                   b.returnDueDate as returnDueDate,
                   b.returnedAt as returnedAt,
                   b.status as status
                from BookLending b
                inner join b.bookInstance bi
                inner join b.readersCard rc
            where b.id = :id and rc.user = :user
        """)
    Optional<BookLendingProjection> findByIdAndUser(UUID id, User user);

    @Query("""
            select b.id as id,
                   bi.book.id as bookId,
                   b.reservedAt as reservedAt,
                   b.borrowedAt as borrowedAt,
                   b.returnDueDate as returnDueDate,
                   b.returnedAt as returnedAt,
                   b.status as status
                from BookLending b
                inner join b.bookInstance bi
                inner join b.readersCard rc
            where b.id = :id
        """)
    Optional<BookLendingProjection> findByIdAsProjection(UUID id);

    Optional<BookLending> findByIdAndReadersCardUser(UUID id, User user);

    Optional<BookLending> findByIdAndStatus(UUID id, LendingStatus status);
}
