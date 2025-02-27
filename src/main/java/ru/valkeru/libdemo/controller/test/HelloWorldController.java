package ru.valkeru.libdemo.controller.test;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.event.SystemEventPublisher;
import ru.valkeru.libdemo.model.dto.AuthorDto;

//@Hidden
@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HelloWorldController {

    SystemEventPublisher systemEventPublisher;

    @Hidden
    @Async
    @GetMapping("/event")
    public void raiseEvent() {
        systemEventPublisher.publishEvent("Test it");
    }

    @GetMapping("/test-object")
    public void testObject(@ParameterObject Pageable pageable) {

    }
}
