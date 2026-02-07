package ru.valkeru.libdemo.util;

import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class CustomTuple implements Tuple {

    private final List<CustomTupleElementImpl<?>> elements = new ArrayList<>();

    private CustomTuple(Object ...arguments) {
        int len = arguments.length / 2;
        for (int i = 0; i < len; i++) {
            int aliasIndex = i * 2;
            int valueIndex = aliasIndex + 1;

            String alias = (String) arguments[aliasIndex];
            if (StringUtils.isBlank(alias)) {
                throw new IllegalArgumentException("Alias is blank on position number [%d]".formatted(aliasIndex));
            }

            Object value = arguments[valueIndex];

            Class<?> valueClass = value != null ? value.getClass() : Object.class;
            elements.add(new CustomTupleElementImpl<>(alias, value, valueClass));
        }
    }

    /**
     * Создаёт кортеж с переданными алиасами и значениями.
     * Формат: key value ... key value
     */
    public static CustomTuple of(Object ...arguments) {
        Assert.notEmpty(arguments, "Arguments must be not empty");

        int argsLength = arguments.length;
        if (argsLength % 2 != 0) {
            throw new IllegalArgumentException("Передано нечётное количество аргументов. Формат: key value ... key value");
        }

        return new CustomTuple(arguments);
    }

    @Override
    public <X> X get(TupleElement<X> tupleElement) {
        return ((CustomTupleElementImpl<X>) tupleElement).getValue();
    }

    @Override
    public <X> X get(String alias, Class<X> type) {
        return type.cast(get(alias));
    }

    @Override
    public Object get(String alias) {
        for (CustomTupleElementImpl<?> element : elements) {
            if (element.getAlias().equals(alias)) {
                return element.getValue();
            }
        }

        throw new IllegalArgumentException("Alias [%s] did not corresponding to any tuple element".formatted(alias));
    }

    @Override
    public <X> X get(int i, Class<X> type) {
        return type.cast(get(i));
    }

    @Override
    public Object get(int i) {
        if (i < 0 || i >= elements.size()) {
            throw new IllegalArgumentException("Index [%d] is out of range of tuple size [%d]".formatted(i, elements.size()));
        }

        return elements.get(i).getValue();
    }

    @Override
    public Object[] toArray() {
        return elements.stream()
                .map(CustomTupleElementImpl::getValue)
                .toArray();
    }

    @Override
    public List<TupleElement<?>> getElements() {
        return List.copyOf(elements);
    }

    @Getter
    @AllArgsConstructor
    private static class CustomTupleElementImpl<T> implements TupleElement<T> {

        private String alias;
        private T value;
        private Class<? extends T> type;

        @Override
        public Class<? extends T> getJavaType() {
            return type;
        }
    }
}
