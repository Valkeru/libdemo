package ru.valkeru.libdemo.config.api;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
    tags = {
        @Tag(name = ApiTags.BOOK, description = "Books"),
        @Tag(name = ApiTags.AUTHOR, description = "Authors"),
        @Tag(name = ApiTags.CYCLE, description = "Cycles"),
        @Tag(name = ApiTags.SERIES, description = "Series"),
        @Tag(name = ApiTags.SERVICE, description = "Service"),
        @Tag(name = ApiTags.SECURITY, description = "Security"),
    }
)
public class ApiConfig {
}
