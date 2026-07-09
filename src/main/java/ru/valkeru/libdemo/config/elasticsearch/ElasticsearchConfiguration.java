package ru.valkeru.libdemo.config.elasticsearch;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.elasticsearch.autoconfigure.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = {"ru.valkeru.libdemo.domain.repository.elasticsearch"})
public class ElasticsearchConfiguration {

    private final ElasticsearchProperties elasticsearchProperties;

    @Bean
    public String elasticsearchIndexPrefix() {
        return "libdemo_";
    }
}
