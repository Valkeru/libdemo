package ru.valkeru.libdemo.domain.repository.jpa.book;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.domain.entity.Book;

import java.util.UUID;

public interface BookRepository extends BaseJpaRepository<Book, UUID>, BookDslRepository {

    @Transactional
    @Modifying
    @Query("delete from Book b where b.id = :id")
    int deleteBookById(UUID id);

    Page<Book> findAll(Pageable pageable);
}
