package ru.valkeru.libdemo.web.api.definition;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name = ApiTags.BOOK, description = "Книги"),
                @Tag(name = ApiTags.AUTHOR, description = "Авторы"),
                @Tag(name = ApiTags.CYCLE, description = "Циклы"),
                @Tag(name = ApiTags.SERIES, description = "Серии"),
                @Tag(name = ApiTags.SERVICE, description = "Служебные"),
                @Tag(name = ApiTags.SECURITY, description = "Безопасность"),
        }
)
public class ApiConfig {
}
