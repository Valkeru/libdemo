package ru.valkeru.libdemo.web.api.definition;


import lombok.experimental.UtilityClass;

@UtilityClass
//@OpenAPIDefinition(
//        tags = {
//                @Tag(name = Tags.SERVICE, description = Tags.Description.SERVICE)
//        }
//)
public class ApiDefinition {

    @UtilityClass
    public static class Tags {

        public static final String SERVICE = "service";
        public static final String SECURITY = "security";

        @UtilityClass
        static class Description {

            static final String SERVICE = "Служебные";
            static final String SECURITY = "Безопасность";
        }
    }

    @UtilityClass
    public static class Summary {

        @UtilityClass
        public static final class Security {

            public static final String SIGN_UP = "Регистрация";
            public static final String SIGN_IN = "Вход";
        }

        @UtilityClass
        public static final class Service {

            public static final String SUMMARY_AUTHOR_ELASTICSEARCH = "Переиндексировать авторов в Elasticsearch";
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
            public static final String ASYNC_TASK_CREATED = "Задание создано";
        }
    }
}
