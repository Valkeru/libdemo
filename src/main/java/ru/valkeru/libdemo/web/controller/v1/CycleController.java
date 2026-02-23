package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.web.api.v1.CycleApi;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.service.infrastructure.application.CycleApplicationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CycleController implements CycleApi {

    private final CycleApplicationService cycleApplicationService;

    @Override
    public ResponseEntity<CycleDto> getCycle(UUID id) {
        return ResponseEntity.ok(cycleApplicationService.getCycleById(id));
    }

    @Override
    public ResponseEntity<Page<CycleDto>> listCycles(Pageable pageable) {
        return ResponseEntity.ok(cycleApplicationService.getAllCycles(pageable));
    }
}
