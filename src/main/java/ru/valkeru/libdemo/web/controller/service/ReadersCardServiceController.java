package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.application.ReadersCardApplicationService;
import ru.valkeru.libdemo.model.dto.ReadersCardCreateDto;
import ru.valkeru.libdemo.web.api.service.ReadersCardServiceApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class ReadersCardServiceController implements ReadersCardServiceApi {

    private final ReadersCardApplicationService service;

    @Override
    public ResponseEntity<Void> createReadersCardStaff(ReadersCardCreateDto dto) {
        UUID cardId = service.createReadersCard(dto);

        return ResponseEntity.created(
                MvcUriComponentsBuilder
                    .fromMethodCall(on(ReadersCardServiceApi.class).getReadersCard(cardId))
                    .build().toUri()
            )
            .build();
    }

    @Override
    public ResponseEntity<Object> getReadersCard(UUID id) {
        return null;
    }
}
