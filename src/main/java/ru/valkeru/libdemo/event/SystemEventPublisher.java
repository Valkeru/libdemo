package ru.valkeru.libdemo.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.event.event.TestEvent;

@Component
@RequiredArgsConstructor
public class SystemEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(String message) {
        TestEvent event = new TestEvent(this, message);

        eventPublisher.publishEvent(event);
    }
}
