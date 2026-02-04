package ru.valkeru.libdemo.model.entity;

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
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.model.entity.base.TimestampedEntity;

import java.util.UUID;

/**
 * Серия — несколько книг, объединённых общим сеттингом и персонажами <br/>
 * Может входить в цикл
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(schema = Database.Schema.LIBRARY, name = "series", indexes = {
        @Index(name = "series_cycle_id_ix", columnList = "cycle_id")
})
public class Series extends TimestampedEntity {

    @Id
    @UuidGenerator
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "cycle_id", foreignKey = @ForeignKey(name = "series_cycle_id_fk"))
    private Cycle cycle;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Version
    @Column(name = "version", nullable = false)
    private long version = 1L;
}
