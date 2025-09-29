package ru.valkeru.libdemo.config.persistence;

import io.hypersistence.utils.common.ReflectionUtils;
import io.hypersistence.utils.hibernate.type.util.ClassImportIntegrator;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class ClassImportIntegratorProvider implements IntegratorProvider {

    private final Set<String> dtoClassesPackages = Set.of(
            "ru.valkeru.libdemo.model.dto"
    );

    @Override
    public List<Integrator> getIntegrators() {
        List<Class<?>> dtoClasses = new ArrayList<>();

        for (String dtoClassesPackage : dtoClassesPackages) {
            log.debug("Try to load DTO projection classes of {}", dtoClassesPackage);

             try {
                 ReflectionUtils.getClassesByPackage(dtoClassesPackage).stream()
                         // Исключение вложенных пакетов
                         .filter(c -> c.getPackage().getName().equals(dtoClassesPackage))
                         .forEach(dtoClasses::add);
             } catch (IllegalStateException ise) {
                 log.error("Failed to load DTO projection classes of {}", dtoClassesPackage, ise);

                 throw ise;
             }
        }

        log.debug("DTO projection classes successfully loaded: {}", dtoClasses);

        return List.of(
                new ClassImportIntegrator(dtoClasses)
        );
    }
}
