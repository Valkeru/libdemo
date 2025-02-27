package ru.valkeru.libdemo.helper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.service.SeriesService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeriesHelper implements ApplicationContextAware {

    static SeriesService seriesService;

    public static boolean cycleHasSeries(Long cycleId) {
        return seriesService.countSeriesByCycleId(cycleId) > 0;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        initDependencies(applicationContext);
    }

    private static void initDependencies(@NonNull ApplicationContext context) throws BeansException {
        seriesService = context.getBean(SeriesService.class);
    }
}
