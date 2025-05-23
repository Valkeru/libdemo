package ru.valkeru.libdemo.api.definition;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.UtilityClass;
import ru.valkeru.libdemo.api.definition.BookDefinition.Tags;

@UtilityClass
@OpenAPIDefinition(
        tags = {
                @Tag(name = Tags.BOOK, description = Tags.Description.BOOK),
        }
)
public class BookDefinition {

    @UtilityClass
    public static class Tags {

        public static final String BOOK = "book";

        @UtilityClass
        static class Description {

            static final String BOOK = "Книги";
        }
    }

    @UtilityClass
    public static class Summary {

        @UtilityClass
        public static final class Book {

            public static final String SUMMARY_CREATE = "Добавить книгу";
            public static final String SUMMARY_UPDATE = "Обновить данные о книге";
            public static final String SUMMARY_VIEW_LIST = "Получить список книг";
            public static final String SUMMARY_VIEW = "Информация о книге";
            public static final String SUMMARY_DELETE = "Удалить книгу";
        }
    }
}
