package ru.valkeru.libdemo.repository.jpa;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.repository.jpa.book.BookDslRepository;
import ru.valkeru.libdemo.repository.jpa.projection.BookShortProjection;

import java.util.UUID;

public interface BookRepository extends BaseJpaRepository<Book, UUID>, BookDslRepository {

    @Transactional
    @Modifying
    @Query("delete from Book b where b.id = :id")
    int deleteBookById(UUID id);

    Page<Book> findAll(Pageable pageable);
}
