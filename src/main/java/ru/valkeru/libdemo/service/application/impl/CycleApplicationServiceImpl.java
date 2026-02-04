package ru.valkeru.libdemo.service.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.mapper.CycleMapper;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.service.core.CycleService;
import ru.valkeru.libdemo.service.application.CycleApplicationService;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CycleApplicationServiceImpl implements CycleApplicationService {

    private final CycleService cycleService;
    private final CycleMapper cycleMapper;

    @Override
    @Transactional
    public CycleDto createOrUpdateCycle(UUID id, CycleDto dto) {
        dto.setId(id);

        Cycle cycle = cycleService.createOrUpdateCycle(dto);

        return cycleMapper.toDto(cycle);
    }

    @Override
    public Collection<CycleDto> getAllCycles() {
        Collection<Cycle> allCycles = cycleService.getAllCycles();

        return cycleMapper.toDtoList(allCycles);
    }

    @Override
    public CycleDto getCycleById(UUID id) {
        Cycle cycle = cycleService.getCycleById(id);

        return cycleMapper.toDto(cycle);
    }

    @Override
    @Transactional
    public void deleteCycleById(UUID id) {
        cycleService.deleteCycleById(id);
    }
}
