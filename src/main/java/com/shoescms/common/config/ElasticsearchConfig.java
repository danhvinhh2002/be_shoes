package com.shoescms.common.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

import java.io.*;

//@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Autowired
    @Lazy
    private ElasticsearchClient elasticsearchClient;

    @Override
    public ClientConfiguration clientConfiguration() {
        HttpHeaders headers = new HttpHeaders();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("credentials/elasticsearch_key.txt"));
            headers.add("Authorization", "ApiKey " + reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ClientConfiguration.builder()
                .connectedTo("35.240.153.93:9200")
                .withDefaultHeaders(headers)
                .build();
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationStarted() throws IOException {
        elasticsearchClient.indices().create(c -> c
                .index("test-indices")
        );
    }

}
