package ru.valkeru.libdemo.repository.jpa;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.UUID;

public interface SeriesRepository extends BaseJpaRepository<Series, UUID> {

    @Modifying
    @Transactional
    @Query("delete from Series s where s.id = :id")
    int deleteSeriesById(UUID id);

    Page<Series> findAll(Pageable pageable);
}
