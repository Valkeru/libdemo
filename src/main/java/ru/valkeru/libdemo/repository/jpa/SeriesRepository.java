package ru.valkeru.libdemo.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.UUID;

public interface SeriesRepository extends JpaRepository<Series, UUID> {

    @Modifying
    @Transactional
    @Query("delete from Series s where s.id = :id")
    int deleteSeriesById(UUID id);
}
