package ru.valkeru.libdemo.serializer;

import ru.valkeru.libdemo.annotation.Secret;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/**
 * Sensitive data serializer to use in logs
 *
 * @see Secret
 *
 * @param <T>
 */
public class SecretSerializer<T> extends ValueSerializer<T> {

    private static final String PATTERN = "*";
    private static final String DEFAULT_VALUE = "<SECRET>";

    private final boolean absolute;

    public SecretSerializer(boolean absolute) {
        this.absolute = absolute;
    }

    public SecretSerializer() {
        this(false);
    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext ctxt, BeanProperty property) {
        if (property != null) {
            Secret annotation = property.getAnnotation(Secret.class);
            if (annotation != null) {
                return new SecretSerializer<>(annotation.absolute());
            }
        }

        return super.createContextual(ctxt, property);
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializationContext context) {
        gen.writeString(getMaskedString(value));
    }

    /**
     * Method to get masked string
     * If masking method for variable type is not defined, default mask value would be returned
     * null value returns as is
     *
     * @param value Value to mask
     * @return Masked string
     */
    private String getMaskedString(T value) {
        if (absolute) {
            return DEFAULT_VALUE;
        }

        return switch (value) {
            case String s -> maskString(s);
            case Number n -> maskNumber(n);
            case null -> null;
            default -> DEFAULT_VALUE;
        };
    }

    /**
     * Strings masking method. Empty string returns as is
     * If string contains of a single symbol it will be replaced with PATTERN. String up to 10 symbols length would be masked up to half string length.
     * For larger strings, first 6 symbols are unmasked
     *
     * @param value Value to mask
     */
    private String maskString(String value) {
        if (value.isBlank()) {
            return value;
        }

        final int length = value.length();
        if (length == 1) {
            return PATTERN;
        }

        final int endIndex = length <= 10 ? length / 2 : 6;

        String start = value.substring(0, endIndex);

        return String.format("%s%s", start, PATTERN.repeat(length - start.length()));
    }

    private String maskNumber(Number value) {
        return maskString(String.valueOf(value));
    }
}
