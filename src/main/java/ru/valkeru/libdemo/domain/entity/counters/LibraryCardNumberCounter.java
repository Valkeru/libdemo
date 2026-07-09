package ru.valkeru.libdemo.domain.entity.counters;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ru.valkeru.libdemo.constants.Database;

@Entity
@Table(schema = Database.SCHEMA_COUNTER, name = "library_card")
public class LibraryCardNumberCounter {

    @Id
    @Column(name = "year")
    private long year;

    private long count;
}
