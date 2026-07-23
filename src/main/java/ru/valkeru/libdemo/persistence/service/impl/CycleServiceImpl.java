package ru.valkeru.libdemo.persistence.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.exception.impl.CycleNotFoundException;
import ru.valkeru.libdemo.mapper.CycleMapper;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.persistence.entity.Cycle;
import ru.valkeru.libdemo.persistence.repository.jpa.CycleRepository;
import ru.valkeru.libdemo.persistence.service.CycleService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CycleServiceImpl implements CycleService {

    private final CycleRepository repository;
    private final CycleMapper mapper;

    @Override
    public Cycle createCycle(CycleDto dto) {
        Cycle cycle = new Cycle();
        mapper.updateCycle(dto, cycle);

        return repository.persist(cycle);
    }

    @Override
    public void updateCycle(CycleDto dto, Cycle cycle) {
        mapper.updateCycle(dto, cycle);

        repository.merge(cycle);
    }

    @Override
    public Optional<Cycle> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Page<Cycle> getAllCycles(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Cycle> getReference(UUID id) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }

        return Optional.of(repository.getReferenceById(id));
    }

    @Override
    public void deleteCycleById(UUID id) {
        if (repository.deleteCycleById(id) == 0) {
            throw CycleNotFoundException.cycleNotFound(id);
        }
    }
}
