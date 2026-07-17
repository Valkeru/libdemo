package ru.valkeru.libdemo;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.valkeru.libdemo.config.system.SystemConfiguration;
import ru.valkeru.libdemo.constants.AppEnvironment;

import java.util.UUID;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(
    basePackages = {
        "ru.valkeru.libdemo.domain.repository.jpa",
        "ru.valkeru.libdemo.infrastructure.repository"
    },
    repositoryBaseClass = BaseJpaRepositoryImpl.class
)
@EnableConfigurationProperties({
    SystemConfiguration.class,
})
@EnableScheduling
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class LibdemoApplication {

    public static void main(String[] args) {
        System.setProperty(AppEnvironment.ENV_INSTANCE_ID, UUID.randomUUID().toString());

        new SpringApplicationBuilder(LibdemoApplication.class)
            .run();
    }
}
