package ru.valkeru.libdemo.service.application;

import ru.valkeru.libdemo.model.dto.CycleDto;

import java.util.Collection;
import java.util.UUID;

public interface CycleApplicationService {

    CycleDto createOrUpdateCycle(UUID id, CycleDto dto);

    Collection<CycleDto> getAllCycles();

    CycleDto getCycleById(UUID id);

    void deleteCycleById(UUID id);
}
