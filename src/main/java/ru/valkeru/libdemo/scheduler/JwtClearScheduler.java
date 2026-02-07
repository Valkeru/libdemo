package ru.valkeru.libdemo.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.service.core.security.JWTService;

@Component
@RequiredArgsConstructor
public class JwtClearScheduler {

    private final JWTService JWTService;

    @Scheduled(cron = "0 */5 * * * *")
    public void schedule() {
        JWTService.deleteExpiredTokens();
    }
}
