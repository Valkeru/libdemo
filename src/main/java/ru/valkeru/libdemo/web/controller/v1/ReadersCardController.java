package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.model.dto.ReadersCardDto;
import ru.valkeru.libdemo.model.dto.ReadersCardListDto;
import ru.valkeru.libdemo.model.dto.internal.ReadersCardCreateResult;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.internal.TokenDto;
import ru.valkeru.libdemo.application.ReadersCardApplicationService;
import ru.valkeru.libdemo.web.api.v1.ReadersCardApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class ReadersCardController implements ReadersCardApi {

    private final ReadersCardApplicationService service;

    @Override
    public ResponseEntity<Void> createReadersCardUser(LibraryPrincipal principal) {
        ReadersCardCreateResult result = service.createReadersCard(principal);

        ResponseEntity.BodyBuilder builder = ResponseEntity.created(
                MvcUriComponentsBuilder.fromMethodCall(
                    on(ReadersCardApi.class).getReadersCard(result.cardId(), principal)
                ).build().toUri()
            );

        TokenDto token = result.token();
        if (token != null) {
            builder
                .header(ApiConfig.ACCESS_TOKEN_HEADER_NAME, token.getAccessToken())
                .header(ApiConfig.REFRESH_TOKEN_HEADER_NAME, token.getRefreshToken());
        }

        return builder.build();
    }

    @Override
    public ResponseEntity<Page<ReadersCardListDto>> getReadersCards(LibraryPrincipal principal, Pageable pageable) {
        return ResponseEntity.ok(service.getUserCards(principal, pageable));
    }

    @Override
    public ResponseEntity<ReadersCardDto> getCurrentReadersCard(LibraryPrincipal principal) {
        return ResponseEntity.ok(service.getCurrentCard(principal));
    }

    @Override
    public ResponseEntity<ReadersCardDto> getReadersCard(UUID id, LibraryPrincipal principal) {
        return ResponseEntity.ok(service.getReadersCard(principal, id));
    }
}
