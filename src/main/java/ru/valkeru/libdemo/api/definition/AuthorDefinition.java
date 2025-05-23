package ru.valkeru.libdemo.api.definition;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.UtilityClass;

@UtilityClass
@OpenAPIDefinition(
        tags = {
                @Tag(name = AuthorDefinition.Tags.AUTHOR, description = AuthorDefinition.Tags.Description.AUTHOR)
        }
)
public class AuthorDefinition {

    @UtilityClass
    public static class Tags {

        public static final String AUTHOR = "author";

        @UtilityClass
        static class Description {

            static final String AUTHOR = "Авторы";
        }
    }

    @UtilityClass
    public static class Summary {

        @UtilityClass
        public static final class Author {

            public static final String SUMMARY_CREATE = "Добавить данные об авторе";
            public static final String SUMMARY_UPDATE = "Обновить данные об авторе";
            public static final String SUMMARY_VIEW_LIST = "Получить список авторов";
            public static final String SUMMARY_VIEW = "Получить данные об авторе";
            public static final String SUMMARY_DELETE = "Удалить данные об авторе";
        }
    }
}
