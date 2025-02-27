package ru.valkeru.libdemo.service.http;

import ru.valkeru.libdemo.model.dto.CycleDto;

import java.util.Collection;

public interface CycleHttpService {

    CycleDto createOrUpdateCycle(Long id, CycleDto dto);

    Collection<CycleDto> getAllCycles();

    CycleDto getCycleById(Long id);

    void deleteCycleById(Long id);
}
