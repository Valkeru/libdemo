package ru.valkeru.libdemo.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import com.fasterxml.jackson.databind.Module;

import java.io.IOException;

@Configuration
public class SerializationConfig {

    @Bean
    public Module springDataPageModule() {
        return new SimpleModule().addSerializer(Page.class, new JsonSerializer<>() {

            @Override
            public void serialize(Page value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();

                gen.writeFieldName("content");
                serializers.defaultSerializeValue(value.getContent(), gen);

                gen.writeNumberField("totalElements",value.getTotalElements());
                gen.writeNumberField("totalPages", value.getTotalPages());
                gen.writeNumberField("number", value.getNumber());
                gen.writeNumberField("size", value.getSize());
                gen.writeBooleanField("first", value.isFirst());
                gen.writeBooleanField("last", value.isLast());
                gen.writeObjectField("sort", value.getSort());
                gen.writeObjectField("pageable", value.getPageable());
                gen.writeBooleanField("empty", value.isEmpty());

                gen.writeEndObject();
            }
        });
    }
}
