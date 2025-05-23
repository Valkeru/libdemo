package ru.valkeru.libdemo.service.http.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.service.CycleService;
import ru.valkeru.libdemo.service.http.CycleHttpService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CycleHttpServiceImpl implements CycleHttpService {

    CycleService cycleService;

    @Override
    public CycleDto createOrUpdateCycle(Long id, CycleDto dto) {
        dto.setId(id);

        return cycleService.createOrUpdateCycle(dto);
    }

    @Override
    public Collection<CycleDto> getAllCycles() {
        return cycleService.getAllCycles();
    }

    @Override
    public CycleDto getCycleById(Long id) {
        return cycleService.getCycleById(id);
    }

    @Override
    public void deleteCycleById(Long id) {
        cycleService.deleteCycleById(id);
    }
}
