package ru.valkeru.libdemo.api.definition;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.UtilityClass;
import ru.valkeru.libdemo.api.definition.SeriesDefinition.Tags;

@UtilityClass
@OpenAPIDefinition(
        tags = {
                @Tag(name = Tags.SERIES, description = Tags.Description.SERIES),
        }
)
public class SeriesDefinition {

    @UtilityClass
    public static class Tags {

        public static final String SERIES = "series";

        @UtilityClass
        static class Description {

            static final String SERIES = "Серии";
        }
    }

    @UtilityClass
    public static class Summary {

        @UtilityClass
        public static final class Series {

            public static final String SUMMARY_CREATE = "Создать серию";
            public static final String SUMMARY_UPDATE = "Обновить серию";
            public static final String SUMMARY_VIEW_LIST = "Получить все серии";
            public static final String SUMMARY_VIEW = "Данные о серии";
            public static final String SUMMARY_DELETE = "Удалить серию";
        }
    }
}
