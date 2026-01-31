package ru.valkeru.libdemo.web.api.definition;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.UtilityClass;

@UtilityClass
@OpenAPIDefinition(
        tags = {
                @Tag(name = CycleDefinition.Tags.CYCLE, description = CycleDefinition.Tags.Description.CYCLE),
        }
)
public class CycleDefinition {

    @UtilityClass
    public static class Tags {

        public static final String CYCLE = "cycle";

        @UtilityClass
        static class Description {

            static final String CYCLE = "Циклы";}
    }

    @UtilityClass
    public static class Summary {

        @UtilityClass
        public static final class Cycle {

            public static final String SUMMARY_CREATE = "Добавить цикл";
            public static final String SUMMARY_UPDATE = "Обновить цикл";
            public static final String SUMMARY_VIEW_LIST = "Получить все циклы";
            public static final String SUMMARY_VIEW = "Получить цикл";
            public static final String SUMMARY_DELETE = "Удалить цикл";
        }


    }
}
