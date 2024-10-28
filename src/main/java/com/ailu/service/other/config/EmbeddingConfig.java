package com.ailu.service.other.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;
import jakarta.annotation.Resource;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 嵌入模型和嵌入存储设置
 * @Author: ailu
 * @Date: 2024/10/26 下午2:02
 */

@Configuration
public class EmbeddingConfig {

    @Resource(name = "freeThreadPoolExecutor")
    private ThreadPoolExecutor freeThreadPoolExecutor;

    @Bean
    public EmbeddingModel embeddedModel() {
        //freeThreadPoolExecutor是线程池，用于并行化
        return new AllMiniLmL6V2EmbeddingModel(freeThreadPoolExecutor);
    }
    @Bean(name = "inMemoryEmbeddingStore")
    public EmbeddingStore<TextSegment> inMemoryEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean(name = "neo4jEmbeddingStore")
    public EmbeddingStore<TextSegment> neo4jEmbeddingStore() {
        EmbeddingStore<TextSegment> embeddingStore = Neo4jEmbeddingStore.builder()
                .withBasicAuth("bolt://localhost:7687/", "neo4j", "dfDF51d5")
                .dimension(384)

                .build();
        return embeddingStore;
    }

}
