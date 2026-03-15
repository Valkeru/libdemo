package ru.valkeru.libdemo.model.entity;

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
import org.hibernate.annotations.UuidGenerator;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.model.entity.base.TimestampedEntity;

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
public class Book extends TimestampedEntity {

    @Id
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @NotBlank
    @Size(max = 17, min = 17)
    @Pattern(regexp = "\\d{3}-\\d-\\d{2}-\\d{6}-\\d")
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
    private long version = 1L;

    @ManyToMany(fetch = FetchType.LAZY)
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
