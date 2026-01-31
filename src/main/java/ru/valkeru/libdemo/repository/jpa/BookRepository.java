package ru.valkeru.libdemo.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Book;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Transactional
    @Modifying
    @Query("delete from Book b where b.id = :id")
    int deleteBookById(UUID id);
}
