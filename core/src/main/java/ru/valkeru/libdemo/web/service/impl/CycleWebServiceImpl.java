package ru.valkeru.libdemo.web.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.mapper.CycleMapper;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.service.base.CycleService;
import ru.valkeru.libdemo.web.service.base.CycleWebService;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CycleWebServiceImpl implements CycleWebService {

    private final CycleService cycleService;
    private final CycleMapper cycleMapper;

    @Override
    public CycleDto createOrUpdateCycle(UUID id, CycleDto dto) {
        dto.setId(id);

        return cycleService.createOrUpdateCycle(dto);
    }

    @Override
    public Collection<CycleDto> getAllCycles() {
        return cycleService.getAllCycles();
    }

    @Override
    public CycleDto getCycleById(UUID id) {
        Cycle cycle = cycleService.getCycleById(id);

        return cycleMapper.toDto(cycle);
    }

    @Override
    public void deleteCycleById(UUID id) {
        cycleService.deleteCycleById(id);
    }
}
