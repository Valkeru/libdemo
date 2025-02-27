package ru.valkeru.libdemo.api.definition;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.UtilityClass;
import ru.valkeru.libdemo.api.definition.ApiDefinition.Tags;

@UtilityClass
@OpenAPIDefinition(
        // Порядок тегов в списке определяет порядок в Swagger UI!
        tags = {
                @Tag(name = Tags.AUTHOR, description = Tags.Description.AUTHOR),
                @Tag(name = Tags.CYCLE, description = Tags.Description.CYCLE),
                @Tag(name = Tags.SERIES, description = Tags.Description.SERIES),
                @Tag(name = Tags.BOOK, description = Tags.Description.BOOK),
                @Tag(name = Tags.REPORT, description = Tags.Description.REPORT),
        }
)
public class ApiDefinition {

    @UtilityClass
    public static class Tags {

        public static final String AUTHOR = "author";
        public static final String SERIES = "series";
        public static final String CYCLE = "cycle";
        public static final String BOOK = "book";
        public static final String REPORT = "report";

        @UtilityClass
        static class Description {

            static final String AUTHOR = "Авторы";
            static final String SERIES = "Серии";
            static final String CYCLE = "Циклы";
            static final String BOOK = "Книги";
            static final String REPORT = "Отчёты";
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

        @UtilityClass
        public static final class Cycle {

            public static final String SUMMARY_CREATE = "Добавить цикл";
            public static final String SUMMARY_UPDATE = "Обновить цикл";
            public static final String SUMMARY_VIEW_LIST = "Получить все циклы";
            public static final String SUMMARY_VIEW = "Получить цикл";
            public static final String SUMMARY_DELETE = "Удалить цикл";
        }

        @UtilityClass
        public static final class Series {

            public static final String SUMMARY_CREATE = "Создать серию";
            public static final String SUMMARY_UPDATE = "Обновить серию";
            public static final String SUMMARY_VIEW_LIST = "Получить все серии";
            public static final String SUMMARY_VIEW = "Данные о серии";
            public static final String SUMMARY_DELETE = "Удалить серию";
        }

        @UtilityClass
        public static final class Book {

            public static final String SUMMARY_CREATE = "Добавить книгу";
            public static final String SUMMARY_UPDATE = "Обновить данные о книге";
            public static final String SUMMARY_VIEW_LIST = "Получить список книг";
            public static final String SUMMARY_VIEW = "Информация о книге";
            public static final String SUMMARY_DELETE = "Удалить книгу";
        }

        @UtilityClass
        public static final class Report {

            public static final String SUMMARY_GET = "Загрузить отчёт";
        }
    }

    @UtilityClass
    public static class SchemaIdDescription {

        public static final String DEFAULT_PATH_ID_TYPE = "integer";
        public static final String DEFAULT_PATH_ID_FORMAT = "int64";

        public static final String AUTHOR_ID = "ID автора";
        public static final String CYCLE_ID = "ID цикла";
        public static final String SERIES_ID = "ID серии";
        public static final String BOOK_ID = "ID книги";
    }

    @UtilityClass
    public static class StatusCodes {

        public static final String OK = "200";
        public static final String CREATED = "201";
        public static final String NO_CONTENT = "204";
        public static final String NOT_FOUND = "404";
        public static final String BAD_REQUEST = "400";
        public static final String CONFLICT = "409";

        @UtilityClass
        public static class Description {

            public static final String OK = "Успех";
            public static final String CREATED = "Успешно создано";
            public static final String NOT_FOUND = "Данные не найдены";
            public static final String BAD_REQUEST = "Некорректный запрос";
            public static final String DATA_INTEGRITY_CONFLICT = "Нарушение целостности данных";
        }
    }
}
