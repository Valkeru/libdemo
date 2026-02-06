package ru.valkeru.libdemo.service.infrastructure.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import ru.valkeru.libdemo.model.dto.CycleDto;

import java.util.UUID;

public interface CycleApplicationService {

    CycleDto createOrUpdateCycle(UUID id, CycleDto dto);

    PagedModel<CycleDto> getAllCycles(Pageable pageable);

    CycleDto getCycleById(UUID id);

    void deleteCycleById(UUID id);
}
