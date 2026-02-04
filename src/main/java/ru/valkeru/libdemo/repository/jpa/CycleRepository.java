package ru.valkeru.libdemo.repository.jpa;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Cycle;

import java.util.Collection;
import java.util.UUID;

public interface CycleRepository extends BaseJpaRepository<Cycle, UUID> {

    @Modifying
    @Transactional
    @Query("delete from Cycle c where c.id = :id")
    int deleteCycleById(UUID id);

    Page<Cycle> findAll(Pageable pageable);
}
