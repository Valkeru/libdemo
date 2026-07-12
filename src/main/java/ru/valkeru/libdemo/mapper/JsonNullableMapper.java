package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class JsonNullableMapper {

    public <T> T getValue(JsonNullable<T> nullable) {
        if (!nullable.isPresent()) {
            return null;
        }

        return nullable.get();
    }
}
