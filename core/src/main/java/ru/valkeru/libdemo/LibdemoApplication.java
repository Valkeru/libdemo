package ru.valkeru.libdemo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.valkeru.libdemo.config.system.SystemConfiguration;
import ru.valkeru.libdemo.constants.AppEnvironment;

import java.util.UUID;

@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ru.valkeru.libdemo.repository.jpa"})
@EnableConfigurationProperties({
        SystemConfiguration.class
})
public class LibdemoApplication {

    public static void main(String[] args) {
        System.setProperty(AppEnvironment.ENV_INSTANCE_ID, UUID.randomUUID().toString());

        new SpringApplicationBuilder(LibdemoApplication.class)
                .run();
    }
}
