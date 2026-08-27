package ru.valkeru.libdemo.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.application.LibraryCardApplicationService;

@Component
@RequiredArgsConstructor
public class LibraryCardExpirationScheduler {

    private final LibraryCardApplicationService libraryCardApplicationService;

    @Scheduled(cron = "0 0 0 * * *")
    public void schedule() {

    }
}
