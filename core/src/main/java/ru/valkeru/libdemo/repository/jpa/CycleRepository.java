package ru.valkeru.libdemo.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.Cycle;

import java.util.UUID;

public interface CycleRepository extends JpaRepository<Cycle, UUID> {

    @Modifying
    @Transactional
    @Query("delete from Cycle c where c.id = :id")
    int deleteCycleById(UUID id);
}
