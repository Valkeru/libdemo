package ru.valkeru.libdemo.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.application.SecurityApplicationService;

@Component
@RequiredArgsConstructor
public class JwtClearScheduler {

    private final SecurityApplicationService securityService;

    @Scheduled(cron = "0 */5 * * * *")
    public void schedule() {
        securityService.deleteExpiredTokens();
    }
}
