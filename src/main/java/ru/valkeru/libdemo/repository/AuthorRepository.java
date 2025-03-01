package ru.valkeru.libdemo.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.entity.Author;

import java.util.Collection;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(
            nativeQuery = true,
            value =
                    """
                                SELECT test_list.id
                                FROM (VALUES (:testIds)) AS test_list(id)
                                WHERE NOT EXISTS(SELECT * FROM library.author a WHERE a.id = test_list.id)
                            """)
    Collection<Long> getIdNotExisted(Collection<Long> testIds);

    @Nonnull
    Collection<Author> getAuthorsByIdIn(Collection<Long> ids, Sort sort);

    @Query("""
            select new AuthorDto(
                a.id,
                a.firstName,
                a.middleName,
                a.lastName
            )
            from Author a
    """)
    Collection<AuthorDto> getAuthors(Sort sort);

    @Transactional
    @Modifying
    @Query("delete from Author where id = :id")
    int deleteAuthorById(Long id);

    @Query("select a from Author a join a.books b where b.id = :#{#bookDto.id}")
    Collection<Author> getAllByBook(BookDto bookDto);
}
