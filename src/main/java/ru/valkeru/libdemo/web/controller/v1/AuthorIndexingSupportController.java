package ru.valkeru.libdemo.web.controller.v1;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.application.AuthorApplicationService;
import ru.valkeru.libdemo.web.api.service.AuthorIndexingSupportApi;

@RestController
@ConditionalOnProperty(value = "app.system.elasticsearch-enabled", havingValue = "true")
public class AuthorIndexingSupportController extends AuthorController implements AuthorIndexingSupportApi {

    public AuthorIndexingSupportController(AuthorApplicationService authorService) {
        super(authorService);
    }

    @Override
    public ResponseEntity<Void> elasticsearchReindexAuthors() {
        authorService.reindexAuthors();

        return ResponseEntity.noContent().build();
    }
}
