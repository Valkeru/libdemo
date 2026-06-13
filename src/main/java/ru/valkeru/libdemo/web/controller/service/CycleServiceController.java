package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.service.application.CycleApplicationService;
import ru.valkeru.libdemo.web.api.service.CycleServiceApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CycleServiceController implements CycleServiceApi {

    private final CycleApplicationService cycleApplicationService;

    @Override
    public ResponseEntity<Void> addCycle(CycleDto cycleDto) {
        UUID id = cycleApplicationService.createCycle(cycleDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(CustomHeaders.RESOURCE_ID, id.toString())
                .build();
    }

    @Override
    public ResponseEntity<Void> updateCycle(UUID id, CycleDto cycleDto) {
        cycleApplicationService.updateCycle(id, cycleDto);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteCycle(UUID id) {
        cycleApplicationService.deleteCycleById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
