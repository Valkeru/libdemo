package ru.valkeru.libdemo.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class SqlUtil {

    public Sort sortByCreatedAtAsc() {
        return Sort.by(Sort.Direction.ASC, "createdAt");
    }

    public Sort sortByIdAsc() {
        return Sort.by(Sort.Direction.ASC, "id");
    }
}
