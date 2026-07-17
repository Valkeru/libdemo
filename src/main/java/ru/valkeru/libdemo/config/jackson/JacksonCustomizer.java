package ru.valkeru.libdemo.config.jackson;

import org.openapitools.jackson.nullable.JsonNullableJackson3Module;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonCustomizer {

//    @Bean
    public JsonMapperBuilderCustomizer jsonCustomizer() {
        return jsonMapperBuilder -> jsonMapperBuilder.addModule(new JsonNullableJackson3Module());
    }
}
