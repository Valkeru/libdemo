package ru.valkeru.libdemo.service;

import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;

import java.util.Collection;

public interface CycleService {

    CycleDto createOrUpdateCycle(CycleDto dto);

    Collection<CycleDto> getAllCycles();

    CycleDto getCycleById(Long id);

    Cycle getCycleEntityById(Long id);

    void deleteCycleById(Long id);
}
