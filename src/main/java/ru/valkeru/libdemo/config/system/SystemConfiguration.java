package ru.valkeru.libdemo.config.system;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.system")
@Getter
@RequiredArgsConstructor
public class SystemConfiguration {

    private final boolean elasticsearchEnabled;
}
