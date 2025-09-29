package ru.valkeru.libdemo.event.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestEvent extends ApplicationEvent {

    String test;

    public TestEvent(Object source, String test) {
        super(source);
        this.test = test;
    }
}
