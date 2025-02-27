package ru.valkeru.libdemo.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.event.event.TestEvent;

@Component
@Slf4j
public class FirstTestEventListener implements ApplicationListener<TestEvent> {

    @Async
    @Override
    @Order(0)
    public void onApplicationEvent(TestEvent event) {
        log.error(String.format("Event listener 1: %s", event.getTest()));
    }
}
