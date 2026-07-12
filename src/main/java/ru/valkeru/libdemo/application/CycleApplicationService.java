package ru.valkeru.libdemo.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.CycleDto;

import java.util.UUID;

public interface CycleApplicationService {

    UUID createCycle(CycleDto cycleDto);

    void updateCycle(UUID id, CycleDto dto);

    Page<CycleDto> getAllCycles(Pageable pageable);

    CycleDto getCycleById(UUID id);

    void deleteCycleById(UUID id);
}
