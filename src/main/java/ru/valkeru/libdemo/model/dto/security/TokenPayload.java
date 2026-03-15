package ru.valkeru.libdemo.model.dto.security;

import java.util.List;

public record TokenPayload(
    String subject,
    List<String> authorities
) {
}
