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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.valkeru.libdemo.constants.Database;

import java.time.Instant;
import java.util.UUID;

/**
 * A cycle is several books with a common setting but differs in a plot
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = Database.SCHEMA_LIBRARY, name = "cycle")
public class Cycle {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "name", columnDefinition = "text")
    private String title;

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
}
