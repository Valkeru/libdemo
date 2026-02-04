package ru.valkeru.libdemo.service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;

import java.util.UUID;

public interface CycleService {

    Cycle createOrUpdateCycle(CycleDto dto);

    Page<Cycle> getAllCycles(Pageable pageable);

    Cycle getCycleById(UUID id);

    void deleteCycleById(UUID id);
}
