package ru.valkeru.libdemo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.model.entity.base.TimestampedEntity;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(schema = Database.Schema.LIBRARY, name = "author", uniqueConstraints = {
        @UniqueConstraint(
                name = Database.Table.Author.CONSTRAINT_FULL_NAME,
                columnNames = {"first_name", "middle_name", "last_name"}
        )
})
public class Author extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    UUID id;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "middle_name")
    String middleName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Version
    @Column(name = "version", nullable = false)
    private long version = 1L;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @ToString.Exclude
    Set<Book> books;
}
