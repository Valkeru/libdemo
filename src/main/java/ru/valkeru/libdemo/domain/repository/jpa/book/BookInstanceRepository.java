package ru.valkeru.libdemo.domain.repository.jpa.book;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.domain.entity.BookInstance;

import java.util.UUID;

public interface BookInstanceRepository extends BaseJpaRepository<BookInstance, UUID> {

    Page<BookInstance> findAllByBookId(UUID bookId, Pageable pageable);
}
