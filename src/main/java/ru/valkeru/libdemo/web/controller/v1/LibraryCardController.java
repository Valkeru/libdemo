package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.model.dto.LibraryCardDto;
import ru.valkeru.libdemo.model.dto.LibraryCardListDto;
import ru.valkeru.libdemo.model.dto.internal.LibraryCardCreateResult;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.internal.TokenDto;
import ru.valkeru.libdemo.application.LibraryCardApplicationService;
import ru.valkeru.libdemo.web.api.v1.LibraryCardApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class LibraryCardController implements LibraryCardApi {

    private final LibraryCardApplicationService service;

    @Override
    public ResponseEntity<Void> createLibraryCardUser(LibraryPrincipal principal) {
        LibraryCardCreateResult result = service.createLibraryCard(principal);

        ResponseEntity.BodyBuilder builder = ResponseEntity.created(
                MvcUriComponentsBuilder.fromMethodCall(
                    on(LibraryCardApi.class).getLibraryCard(result.cardId(), principal)
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
    public ResponseEntity<Page<LibraryCardListDto>> getLibraryCards(LibraryPrincipal principal, Pageable pageable) {
        return ResponseEntity.ok(service.getUserCards(principal, pageable));
    }

    @Override
    public ResponseEntity<LibraryCardDto> getCurrentLibraryCard(LibraryPrincipal principal) {
        return ResponseEntity.ok(service.getCurrentCard(principal));
    }

    @Override
    public ResponseEntity<LibraryCardDto> getLibraryCard(UUID id, LibraryPrincipal principal) {
        return ResponseEntity.ok(service.getLibraryCard(principal, id));
    }
}
