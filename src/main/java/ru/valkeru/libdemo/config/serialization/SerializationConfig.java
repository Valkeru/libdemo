package ru.valkeru.libdemo.config.serialization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedModel;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

@Configuration
public class SerializationConfig {

    @Bean
    public JacksonModule springPageModelSerializer() {

        return new SimpleModule().addSerializer(PagedModel.class, new ValueSerializer<>() {

            @Override
            public void serialize(PagedModel value, JsonGenerator gen, SerializationContext ctxt) {
                gen.writeStartObject();

                gen.writeName("content");
                ctxt.writeValue(gen, value.getContent());

                writePageObject(value, gen);


                gen.writeEndObject();
            }
        });
    }

    private static void writePageObject(PagedModel<?> value, JsonGenerator gen) {
        gen.writeName("page");
        gen.writeStartObject();

        gen.writeName("size");
        gen.writeNumber(value.getMetadata().size());

        gen.writeName("number");
        gen.writeNumber(value.getMetadata().number());

        gen.writeName("totalElements");
        gen.writeNumber(value.getMetadata().totalElements());

        gen.writeName("totalPages");
        gen.writeNumber(value.getMetadata().totalPages());

        gen.writeEndObject();
    }
}
