package ru.valkeru.libdemo.config.elasticsearch;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = {"ru.valkeru.libdemo.domain.repository.elasticsearch"})
public class ElasticsearchConfiguration {

    @Bean("appElasticsearchProperties")
    public ApplicationElasticsearchProperties appElasticsearchProperties() {
        return new ApplicationElasticsearchProperties();
    }
}
