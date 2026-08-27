package ru.valkeru.libdemo.domain.repository.jpa.book;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.BookInstance;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface BookInstanceRepository extends BaseJpaRepository<BookInstance, UUID> {

    Page<BookInstance> findAllByBookId(UUID bookId, Pageable pageable);

    @Query(
        nativeQuery = true,
        value = """
                SELECT bi.id
                    FROM library.book_instance bi
                WHERE bi.book_id = :#{#book.id}
                AND NOT EXISTS(
                    SELECT 1 FROM library.book_lending bb
                        WHERE bb.book_instance_id = bi.id
                        AND status IN (:lockedStatuses)
                )
                FOR UPDATE SKIP LOCKED
                LIMIT 1
            """
    )
    Optional<UUID> reserveAvailableInstance(Book book, Collection<String> lockedStatuses);
}
