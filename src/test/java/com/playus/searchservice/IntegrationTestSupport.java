package com.playus.searchservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    private static final GenericContainer redis;
    private static final ElasticsearchContainer elasticsearch;

    private static final String REDIS_VERSION = "redis:7.0.12";
    private static final String ELASTIC_VERSION = "elasticsearch:9.0.1";

    private static final int REDIS_PORT = 6379;
    private static final int ELASTIC_PORT = 9200;

    static {
        redis = new GenericContainer(DockerImageName.parse(REDIS_VERSION))
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);

        elasticsearch = new ElasticsearchContainer(DockerImageName.parse(ELASTIC_VERSION))
                .withExposedPorts(ELASTIC_PORT)
                .withReuse(true);

        redis.start();
        elasticsearch.start();
    }

    @DynamicPropertySource
    public static void dynamicConfiguration(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> String.valueOf(redis.getMappedPort(REDIS_PORT)));

        registry.add("spring.elasticsearch.uris", () -> String.format("http://%s:%d/", elasticsearch.getHost(), elasticsearch.getMappedPort(ELASTIC_PORT)));
    }
}

