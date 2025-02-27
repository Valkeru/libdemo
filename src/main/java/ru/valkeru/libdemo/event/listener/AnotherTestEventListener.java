package ru.valkeru.libdemo.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.event.event.TestEvent;

@Component
@Slf4j
public class AnotherTestEventListener implements ApplicationListener<TestEvent> {

    @Async
    @Override
    public void onApplicationEvent(TestEvent event) {
        log.error(String.format("Event listener 2: %s", event.getTest()));
    }
}
