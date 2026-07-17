package ru.valkeru.libdemo.web.controller.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.application.AuthorApplicationService;
import ru.valkeru.libdemo.web.api.service.AuthorIndexingSupportServiceApi;

@RestController
@ConditionalOnProperty(value = "app.system.elasticsearch-enabled", havingValue = "true")
public class AuthorIndexingSupportServiceController extends AuthorServiceController implements AuthorIndexingSupportServiceApi {

    public AuthorIndexingSupportServiceController(AuthorApplicationService authorService) {
        super(authorService);
    }

    @Override
    public ResponseEntity<Void> elasticsearchReindexAuthors() {
        authorService.reindexAuthors();

        return ResponseEntity.noContent().build();
    }
}
