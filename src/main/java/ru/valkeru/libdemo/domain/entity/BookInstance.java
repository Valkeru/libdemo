package ru.valkeru.libdemo.domain.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.valkeru.libdemo.constants.Database;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = Database.SCHEMA_LIBRARY, name = "book_instance")
public class BookInstance {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false)
    private UUID id;

    @NotNull
    @Immutable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", updatable = false, nullable = false)
    private Book book;

    @NotBlank
    @Column(name = "inventory_number", nullable = false, length = 25)
    private String inventoryNumber;

    @NotNull
    @Type(JsonBinaryType.class)
    @Column(name = "notes", nullable = false)
    private List<String> notes = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @Column(name = "version", nullable = false)
    private long version;

    @CreationTimestamp
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    private Instant createdAt;

    @UpdateTimestamp
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private Instant updatedAt;
}
