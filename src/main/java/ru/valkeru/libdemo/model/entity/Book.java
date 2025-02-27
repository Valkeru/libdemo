package ru.valkeru.libdemo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.model.entity.base.TimestampedEntity;

import java.util.Set;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(schema = Database.Schema.LIBRARY, name = "book", indexes = {
        @Index(name = "book_name_ix", columnList = "name")
})
public class Book extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Database.Generator.BOOK_ID)
    @SequenceGenerator(
            name = Database.Generator.BOOK_ID,
            schema = Database.Schema.LIBRARY,
            sequenceName = Database.Sequence.BOOK_ID_SEQUENCE,
            allocationSize = Database.SEQUENCE_CACHE
    )
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    String name;

    @Size(max = 17, min = 17)
    @Column(name = "isbn", nullable = false, columnDefinition = "char(17)")
    String isbn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            schema = Database.Schema.LIBRARY,
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            foreignKey = @ForeignKey(name = Database.Table.BookAuthor.BOOK_FK),
            inverseJoinColumns = @JoinColumn(name = "author_id"),
            inverseForeignKey = @ForeignKey(name = Database.Table.BookAuthor.AUTHOR_FK),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = Database.Table.BookAuthor.CONSTRAINT_BOOK_AUTHOR,
                            columnNames = {"book_id", "author_id"}
                    )
            }
    )
    @ToString.Exclude
    Set<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", foreignKey = @ForeignKey(name = Database.Table.Book.CYCLE_FK))
    @ToString.Exclude
    private Cycle cycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", foreignKey = @ForeignKey(name = Database.Table.Book.SERIES_FK))
    @ToString.Exclude
    private Series series;
}
