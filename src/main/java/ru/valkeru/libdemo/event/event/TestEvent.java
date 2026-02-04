package ru.valkeru.libdemo.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TestEvent extends ApplicationEvent {

    private final String test;

    public TestEvent(Object source, String test) {
        super(source);
        this.test = test;
    }
}
