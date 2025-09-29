package ru.valkeru.libdemo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.model.entity.base.TimestampedEntity;

/**
 * Серия — несколько книг, объединённых общим сеттингом и персонажами
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(schema = Database.Schema.LIBRARY, name = "series")
public class Series extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Database.Generator.SERIES_ID)
    @SequenceGenerator(
            name = Database.Generator.SERIES_ID,
            schema = Database.Schema.LIBRARY,
            sequenceName = Database.Sequence.SERIES_ID_SEQUENCE,
            allocationSize = Database.SEQUENCE_CACHE
    )
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    String name;

//    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY)
//    @ToString.Exclude
//    Set<Book> books;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cycle_id", foreignKey = @ForeignKey(name = Database.Table.Series.CYCLE_FK))
//    Cycle cycle;
}
