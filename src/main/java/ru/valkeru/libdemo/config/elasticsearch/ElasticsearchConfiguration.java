package ru.valkeru.libdemo.config.elasticsearch;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.elasticsearch.autoconfigure.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = {"ru.valkeru.libdemo.repository.elasticsearch"})
public class ElasticsearchConfiguration {

    private final ElasticsearchProperties elasticsearchProperties;

    @Bean
    public String elasticsearchIndexPrefix() {
        return "libdemo_";
    }

//    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return new ElasticsearchClient(elasticsearchTransport());
    }

    private ElasticsearchTransport elasticsearchTransport() {
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build()
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        JacksonJsonpMapper mapper = new JacksonJsonpMapper(objectMapper);

        return new RestClientTransport(restClient(), mapper);
    }

//    @Bean
    public RestClient restClient() {
        return RestClient.builder(getHosts())
                .setDefaultHeaders(getDefaultHeaders())
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder
                                .addInterceptorLast(
                                        (HttpResponseInterceptor) (response, context) ->
                                                response.addHeader("X-Elastic-Product", "Elasticsearch")
                ))
                .build();
    }

    private HttpHost[] getHosts() {
        return elasticsearchProperties.getUris().stream()
                .map(HttpHost::create)
                .toList().toArray(new HttpHost[0]);
    }

    private Header[] getDefaultHeaders() {
        Header contentTypeHeader = new BasicHeader(
                HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE
        );

        return List.of(contentTypeHeader).toArray(new Header[0]);
    }
}
