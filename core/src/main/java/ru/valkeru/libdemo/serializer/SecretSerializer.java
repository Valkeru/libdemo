package ru.valkeru.libdemo.serializer;

import ru.valkeru.libdemo.annotation.Secret;
import tools.jackson.core.JsonGenerator;
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

    @Override
    public void serialize(T value, JsonGenerator gen, SerializationContext context) {
        gen.writeString(getMaskedString(value));
    }

    /**
     * Метод возвращает маскированную строку
     * Значения типов, для которых не определены методы, маскируются значением по умолчанию
     * null возвращается как есть
     *
     * @param value Маскируемое значение
     * @return Маскированная строка
     */
    private String getMaskedString(T value) {
        if (value == null) {
            return null;
        }

        return DEFAULT_VALUE;

//        return switch (value) {
//            case String s -> maskString(s);
//            case Number n -> maskNumber(n);
//            case null -> null;
//            default -> DEFAULT_VALUE;
//        };
    }

    /**
     * Метод для маскирования строк. Пустая строка возвращается как есть.
     * Единственный символ в строке заменяется на PATTERN. Строки длиной до 10 символов маскируются по половине длины.
     * Для более длинных строк немаскированными остаются первые 6 символов
     *
     * @param value Маскируемое значение
     * @return Маскированная строка
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
