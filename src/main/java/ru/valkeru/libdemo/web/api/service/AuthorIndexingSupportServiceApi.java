package ru.valkeru.libdemo.web.api.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import ru.valkeru.libdemo.config.api.ApiTags;

public interface AuthorIndexingSupportServiceApi {

    @Operation(
        summary = "Reindex data in Elasticsearch",
        tags = {ApiTags.SERVICE, ApiTags.AUTHOR},
        responses = {
            @ApiResponse(responseCode = "204", description = "Task started")
        }
    )
    @PostMapping("/reindex")
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).SERVICE_INDEXING.name())")
    ResponseEntity<Void> elasticsearchReindexAuthors();
}
