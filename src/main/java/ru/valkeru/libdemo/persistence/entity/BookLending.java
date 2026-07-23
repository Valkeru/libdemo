package ru.valkeru.libdemo.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.persistence.enums.LendingStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = Database.SCHEMA_LIBRARY, name = "book_lending")
public class BookLending {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_instance_id", nullable = false, updatable = false)
    private BookInstance bookInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "readers_card_id", nullable = false, updatable = false)
    private ReadersCard readersCard;

    @Column(name = "reserved_at", columnDefinition = "timestamptz", updatable = false)
    private Instant reservedAt;

    @Column(name = "reservation_due_date")
    private LocalDate reservationDueDate;

    @Column(name = "borrowed_at", columnDefinition = "timestamptz")
    private Instant borrowedAt;

    @Column(name = "return_due_date")
    private LocalDate returnDueDate;

    @Column(name = "returned_at", columnDefinition = "timestamptz")
    private Instant returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private LendingStatus status;

    @Version
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "version", nullable = false)
    private long version;

    @CreationTimestamp
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", columnDefinition = "timestamptz", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "updated_at", columnDefinition = "timestamptz", insertable = false)
    private Instant updatedAt;
}
