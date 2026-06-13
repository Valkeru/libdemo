package ru.valkeru.libdemo.service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;

import java.util.Optional;
import java.util.UUID;

public interface CycleService {

    Cycle createCycle(CycleDto dto);

    void updateCycle(CycleDto dto, Cycle cycle);

    Optional<Cycle> findById(UUID id);

    Page<Cycle> getAllCycles(Pageable pageable);

    void deleteCycleById(UUID id);

    Optional<Cycle> getReference(UUID id);
}
