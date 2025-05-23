package ru.valkeru.libdemo.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.api.definition.ApiDefinition;

@RestController
@RequestMapping("/v1/service")
public interface AuthorServiceApi {

    @Operation(
            summary = ApiDefinition.Summary.Service.SUMMARY_AUTHOR_ELASTICSEARCH,
            tags = ApiDefinition.Tags.SERVICE,
            responses = {
                    @ApiResponse(
                            responseCode = ApiDefinition.StatusCodes.NO_CONTENT,
                            description = ApiDefinition.StatusCodes.Description.ASYNC_TASK_CREATED
                    )
            }
    )
    @PostMapping("/elasticsearch/authors")
    ResponseEntity<Void> elasticsearchReindexAuthors();
}
