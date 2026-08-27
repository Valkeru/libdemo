package ru.valkeru.libdemo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.valkeru.libdemo.constants.Database;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(schema = Database.SCHEMA_LIBRARY, name = "author")
public class Author {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    protected Instant createdAt;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, columnDefinition = "timestamptz")
    protected Instant updatedAt;

    public String getFullName() {
        return StringUtils.joinWith(StringUtils.SPACE, firstName, middleName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (!(o instanceof Author that)) {
            return false;
        }

        if (id == null) {
            return false;
        }

        return id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Author.class.hashCode();
    }
}
