package ru.valkeru.libdemo.model.dto.internal;

import ru.valkeru.libdemo.model.dto.security.TokenDto;

import java.util.UUID;

public record LibraryCardCreateResult (UUID cardId, TokenDto token) {
}
