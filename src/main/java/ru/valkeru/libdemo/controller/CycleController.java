package ru.valkeru.libdemo.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.api.CycleApi;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.service.http.CycleHttpService;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CycleController implements CycleApi {

    CycleHttpService cycleHttpService;

    @Override
    public ResponseEntity<CycleDto> addCycle(CycleDto cycleDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cycleHttpService.createOrUpdateCycle(null, cycleDto));
    }

    @Override
    public ResponseEntity<CycleDto> updateCycle(Long id, CycleDto cycleDto) {
        return ResponseEntity.ok(cycleHttpService.createOrUpdateCycle(id, cycleDto));
    }

    @Override
    public ResponseEntity<CycleDto> getCycle(Long id) {
        return ResponseEntity.ok(cycleHttpService.getCycleById(id));
    }

    @Override
    public ResponseEntity<Collection<CycleDto>> listCycles() {
        return ResponseEntity.ok(cycleHttpService.getAllCycles());
    }

    @Override
    public ResponseEntity<Void> deleteCycle(Long id) {
        cycleHttpService.deleteCycleById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
