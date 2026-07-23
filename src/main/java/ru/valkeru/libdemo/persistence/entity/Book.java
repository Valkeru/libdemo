package ru.valkeru.libdemo.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.valkeru.libdemo.constants.Database;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(schema = Database.SCHEMA_LIBRARY, name = "book", indexes = {
        @Index(name = "book_name_ix", columnList = "name"),
        @Index(name = "book_cycle_id_ix", columnList = "cycle_id"),
        @Index(name = "book_series_id_ix", columnList = "series_id")
})
public class Book {

    @Id
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String title;

    @NotBlank
    @Size(max = 17, min = 17)
    @Column(name = "isbn", nullable = false, columnDefinition = "char(17)")
    private String isbn;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "cycle_id", foreignKey = @ForeignKey(name = "book_cycle_id_fk"))
    private Cycle cycle;

    @ManyToOne
    @JoinColumn(name = "series_id", foreignKey = @ForeignKey(name = "book_series_id_fk"))
    private Series series;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    protected Instant createdAt;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, columnDefinition = "timestamptz")
    protected Instant updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            schema = Database.SCHEMA_LIBRARY,
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            foreignKey = @ForeignKey(name = "book_author_book_id_fk"),
            inverseJoinColumns = @JoinColumn(name = "author_id"),
            inverseForeignKey = @ForeignKey(name = "book_author_author_id_fk"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "book_author_book_id_author_id_uc",
                            columnNames = {"book_id", "author_id"}
                    )
            },
            indexes = {
                    @Index(name = "book_author_book_id_ix", columnList = "book_id"),
                    @Index(name = "book_author_author_id_ix", columnList = "author_id")
            }
    )
    @ToString.Exclude
    private Set<Author> authors;
}
