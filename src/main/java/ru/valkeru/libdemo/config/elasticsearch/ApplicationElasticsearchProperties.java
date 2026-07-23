package ru.valkeru.libdemo.config.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.elasticsearch")
public class ApplicationElasticsearchProperties {

    private String authorIndexName;
}
