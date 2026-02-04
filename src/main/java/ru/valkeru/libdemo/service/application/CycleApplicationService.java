package ru.valkeru.libdemo.service.application;

import org.springframework.data.web.PagedModel;
import ru.valkeru.libdemo.model.dto.CycleDto;

import java.util.UUID;

public interface CycleApplicationService {

    CycleDto createOrUpdateCycle(UUID id, CycleDto dto);

    PagedModel<CycleDto> getAllCycles();

    CycleDto getCycleById(UUID id);

    void deleteCycleById(UUID id);
}
