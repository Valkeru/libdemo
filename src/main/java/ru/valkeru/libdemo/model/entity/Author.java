package ru.valkeru.libdemo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.model.entity.base.TimestampedEntity;

import java.util.UUID;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(schema = Database.Schema.LIBRARY, name = "author", uniqueConstraints = {
        @UniqueConstraint(
                name = "author_full_name_uc",
                columnNames = {"first_name", "middle_name", "last_name"}
        )
})
public class Author extends TimestampedEntity {

    @Id
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    UUID id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "middle_name")
    String middleName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    String lastName;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Version
    @Column(name = "version", nullable = false)
    private long version = 1L;
}
