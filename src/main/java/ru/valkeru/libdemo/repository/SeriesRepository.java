package ru.valkeru.libdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Series;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Modifying
    @Transactional
    @Query("delete from Series s where s.id = :id")
    int deleteSeriesById(Long id);

    int countByCycleId(Long cycleId);
}
