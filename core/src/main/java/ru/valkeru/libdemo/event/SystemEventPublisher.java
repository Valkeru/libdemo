package ru.valkeru.libdemo.event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.event.event.TestEvent;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SystemEventPublisher {

    ApplicationEventPublisher eventPublisher;

    public void publishEvent(String message) {
        TestEvent event = new TestEvent(this, message);

        eventPublisher.publishEvent(event);
    }
}
