package ru.valkeru.libdemo.component.jackson;

import org.apache.commons.lang3.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * Transform spaces in ISBN into hyphens. Empty input strings are deserialized as {@code null}
 */
public class ISBNDeserializer extends ValueDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        String value = p.readValueAs(String.class);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return value.replaceAll("\\s+", "-");
    }
}
