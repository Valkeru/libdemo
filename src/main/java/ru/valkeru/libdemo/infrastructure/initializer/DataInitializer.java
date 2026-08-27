package ru.valkeru.libdemo.infrastructure.initializer;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;
import ru.valkeru.libdemo.constants.ApplicationProfiles;
import ru.valkeru.libdemo.domain.entity.Author;
import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.BookInstance;
import ru.valkeru.libdemo.domain.repository.jpa.author.AuthorRepository;
import ru.valkeru.libdemo.domain.repository.jpa.book.BookInstanceRepository;
import ru.valkeru.libdemo.domain.repository.jpa.book.BookRepository;
import ru.valkeru.libdemo.util.ISBNUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile(ApplicationProfiles.PROFILE_DEVELOP)
public class DataInitializer implements ApplicationRunner {

    private static final String EAN_ISBN_PREFIX = "978";
    private static final String REG_GROUP = "1";
    private static final int publisherPartLength = 2;
    private static final int publicationPartLength = 6;
    private static final Map<String, Integer> publisherCounters = new HashMap<>();

    @Value("${DATA_INITIALIZER_AUTHOR_BATCH_SIZE:100}")
    private int batchSize;

    @Value("${DATA_INITIALIZER_AUTHOR_BATCH_COUNT:100}")
    private int batchCount;

    private final RandomStringUtils randomStringUtils = RandomStringUtils.insecure();
    private final RandomUtils randomUtils = RandomUtils.insecure();

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Override
    public void run(@NonNull ApplicationArguments args) throws Exception {
        if (authorRepository.count() > 0) {
            return;
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Initializing a database...");

        try {
            dropIndices();

            prepareData();
        } catch (Exception e) {
            log.error("Data preparation failed", e);
        } finally {
            restoreIndices();
        }

        stopWatch.stop();
        log.info("Database was initialized successfully in {} sec", stopWatch.getTotalTimeSeconds());
    }

    /**
     * Drop indices to improve performance
     */
    private void dropIndices() {
        jdbcTemplate.execute("""
            DROP INDEX IF EXISTS library.author_first_name_trgm;
            DROP INDEX IF EXISTS library.author_middle_name_trgm;
            DROP INDEX IF EXISTS library.author_last_name_trgm;
            DROP INDEX IF EXISTS library.book_cycle_id_ix;
            DROP INDEX IF EXISTS library.book_name_ix;
            DROP INDEX IF EXISTS library.book_series_id_ix;
            DROP INDEX IF EXISTS library.book_author_author_id_ix;
            DROP INDEX IF EXISTS library.book_instance_book_id_ix;
            ALTER TABLE library.book_instance DROP CONSTRAINT book_instance_inventory_number_uc;
            ALTER TABLE library.book_instance DROP CONSTRAINT book_instance_book_id_fk;
            ALTER TABLE library.book_author DROP CONSTRAINT book_author_book_id_fk;
            ALTER TABLE library.book_author DROP CONSTRAINT book_author_author_id_fk;
            ALTER TABLE library.book DROP CONSTRAINT book_isbn_uc;
            """);
    }

    /**
     * Recreate previously deleted indices
     */
    private void restoreIndices() {
        jdbcTemplate.execute("""
            CREATE INDEX IF NOT EXISTS author_first_name_trgm ON library.author USING gin (first_name public.gin_trgm_ops);
            CREATE INDEX IF NOT EXISTS author_middle_name_trgm ON library.author USING gin (middle_name public.gin_trgm_ops);
            CREATE INDEX IF NOT EXISTS author_last_name_trgm ON library.author USING gin (last_name public.gin_trgm_ops);
            CREATE INDEX IF NOT EXISTS book_cycle_id_ix ON library.book (cycle_id);
            CREATE INDEX IF NOT EXISTS book_name_ix ON library.book (name);
            CREATE INDEX IF NOT EXISTS book_series_id_ix ON library.book (series_id);
            CREATE INDEX IF NOT EXISTS book_author_author_id_ix ON library.book_author (author_id);
            CREATE INDEX IF NOT EXISTS book_instance_book_id_ix ON library.book_instance (book_id);
            ALTER TABLE library.book_instance ADD CONSTRAINT book_instance_inventory_number_uc UNIQUE (inventory_number);
            ALTER TABLE library.book_instance ADD CONSTRAINT book_instance_book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id);
            ALTER TABLE library.book_author ADD CONSTRAINT book_author_book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id);
            ALTER TABLE library.book_author ADD CONSTRAINT book_author_author_id_fk FOREIGN KEY (author_id) REFERENCES library.author (id);
            ALTER TABLE library.book ADD CONSTRAINT book_isbn_uc UNIQUE (isbn)
            """);
    }

    private void prepareData() {
        List<Author> authorsBatch = new ArrayList<>(batchSize);
        List<Book> booksBatch = new ArrayList<>();
        List<BookInstance> bookInstancesBatch = new ArrayList<>();
        AtomicLong inventoryNumber = new AtomicLong();

        int authorsToCreate = batchSize * batchCount;
        for (int i = 1; i <= authorsToCreate; i++) {
            Author author = createAuthor();
            authorsBatch.add(author);

            int booksCount = generateBooksCount();
            for (int b = 0; b < booksCount; b++) {
                Book book = createBook(author);
                booksBatch.add(book);

                int bookInstancesCount = generateBookInstanceCount();
                for (int bi = 0; bi < bookInstancesCount; bi++) {
                    BookInstance bookInstance = createBookInstance(book, inventoryNumber);
                    bookInstancesBatch.add(bookInstance);
                }
            }

            if (i % batchSize == 0) {

                transactionTemplate.executeWithoutResult(ts -> {
                    authorRepository.persistAll(authorsBatch);
                    bookRepository.persistAll(booksBatch);
                    bookInstanceRepository.persistAll(bookInstancesBatch);

                    entityManager.flush();
                    entityManager.clear();
                });
                authorsBatch.clear();
                booksBatch.clear();
                bookInstancesBatch.clear();
            }
        }
    }

    private BookInstance createBookInstance(Book book, AtomicLong inventoryNumber) {
        BookInstance instance = new BookInstance();
        instance.setBook(book);
        instance.setInventoryNumber(String.format("%020d", inventoryNumber.incrementAndGet()));

        return instance;
    }

    private Author createAuthor() {
        Author author = new Author();

        String firstName = AuthorNameGenerator.generateFirstName();
        String lastName = AuthorNameGenerator.generateLastName();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        String middleName = AuthorNameGenerator.generateMiddleName(firstName, lastName);
        author.setMiddleName(StringUtils.trimToNull(middleName));

        return author;
    }

    private Book createBook(Author author) {
        Book book = new Book();
        book.setAuthors(Set.of(author));
        book.setTitle(BookTitleGenerator.generate());

        String isbn = generateIsbn();
        book.setIsbn(isbn);

        return book;
    }

    private String generateIsbn() {
        String publisherCode = randomStringUtils.nextNumeric(publisherPartLength);
        int publication = publisherCounters.merge(publisherCode, 1, Integer::sum) - 1;
        String publicationCode = StringUtils.leftPad(String.valueOf(publication), publicationPartLength, "0");

        String isbnSignificant = StringUtils.join(
            EAN_ISBN_PREFIX,
            REG_GROUP,
            publisherCode,
            publicationCode
        );

        int isbnControl = ISBNUtil.calculateISBN13Control(isbnSignificant);

        return StringUtils.joinWith(
            "-",
            EAN_ISBN_PREFIX,
            REG_GROUP,
            publisherCode,
            publicationCode,
            isbnControl
        );
    }

    private int generateBooksCount() {
        int probe = randomUtils.randomInt(0, 101);

        if (probe < 90) {
            return randomUtils.randomInt(1, 7);
        }

        if (probe < 99) {
            return randomUtils.randomInt(7, 20);
        }

        return randomUtils.randomInt(20, 101);
    }

    private int generateBookInstanceCount() {
        int probe = randomUtils.randomInt(0, 101);

        if (probe < 80) {
            return randomUtils.randomInt(1, 3);
        }

        if (probe < 95) {
            return randomUtils.randomInt(3, 8);
        }

        return randomUtils.randomInt(8, 20);
    }
}
