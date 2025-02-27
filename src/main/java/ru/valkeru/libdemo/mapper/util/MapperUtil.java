package ru.valkeru.libdemo.mapper.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.exception.impl.AuthorNotFoundException;
import ru.valkeru.libdemo.mapper.qualifier.AuthorsByDtoList;
import ru.valkeru.libdemo.mapper.qualifier.CycleById;
import ru.valkeru.libdemo.mapper.qualifier.SeriesById;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.service.AuthorService;
import ru.valkeru.libdemo.service.CycleService;
import ru.valkeru.libdemo.service.SeriesService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MapperUtil implements ApplicationContextAware {

    static AuthorService authorService;
    static SeriesService seriesService;
    static CycleService cycleService;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        initDependencies(applicationContext);
    }

    @AuthorsByDtoList
    public static Set<Author> getAuthorEntityListByDtoList(Collection<AuthorDto> authorDtoCollection) {
        List<Long> ids = authorDtoCollection.stream()
                .map(AuthorDto::getId)
                .toList();

        Collection<Long> notFound = authorService.checkNotExistedIds(ids);
        if (!notFound.isEmpty()) {
            throw AuthorNotFoundException.authorsNotFound(notFound);
        }

        return new HashSet<>(authorService.getAllById(ids));
    }

    @SeriesById
    public static Series getSeriesEntityById(Long seriesId) {
        if (seriesId == null) {
            return null;
        }

        return seriesService.getSeriesEntity(seriesId);
    }

    @CycleById
    public static Cycle getCycleByDto(Long id) {
        if (id == null) {
            return null;
        }

        return cycleService.getCycleEntityById(id);
    }

    private static void initDependencies(ApplicationContext context) throws BeansException {
        authorService = context.getBean(AuthorService.class);
        seriesService = context.getBean(SeriesService.class);
        cycleService = context.getBean(CycleService.class);
    }
}
