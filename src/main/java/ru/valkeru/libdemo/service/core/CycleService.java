package ru.valkeru.libdemo.service.core;

import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;

import java.util.Collection;
import java.util.UUID;

public interface CycleService {

    Cycle createOrUpdateCycle(CycleDto dto);

    Collection<Cycle> getAllCycles();

    Cycle getCycleById(UUID id);

    void deleteCycleById(UUID id);
}
