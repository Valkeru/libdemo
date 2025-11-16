package ru.valkeru.libdemo.service.base;

import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;

import java.util.Collection;
import java.util.UUID;

public interface CycleService {

    CycleDto createOrUpdateCycle(CycleDto dto);

    Collection<CycleDto> getAllCycles();

    Cycle getCycleById(UUID id);

    void deleteCycleById(UUID id);
}
