package ru.valkeru.libdemo.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * A series is several books with a common setting and characters <br/>
 * Series may be a part of a cycle
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = Database.SCHEMA_LIBRARY, name = "series", indexes = {
        @Index(name = "series_cycle_id_ix", columnList = "cycle_id")
})
public class Series {

    @Id
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "cycle_id", foreignKey = @ForeignKey(name = "series_cycle_id_fk"))
    private Cycle cycle;

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
