package cl.lcd.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {
    @Bean
    public ElasticsearchClient client() {
        System.out.println("elastic search configured");
        return ElasticsearchClient.of(b -> b
                .host("http://localhost:9200"));
    }
//    @Bean
//    public ElasticsearchProperties.Restclient restClient() {
//        return RestClient.builder(new HttpHost("localhost", 9200)).build();
//    }
//
//    @Bean
//    public ElasticsearchTransport transport(RestClient client) {
//        return new RestClientTransport(client, new JacksonJsonpMapper());
//    }
//
//    @Bean
//    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
//        return new ElasticsearchClient(transport);
//    }
}
