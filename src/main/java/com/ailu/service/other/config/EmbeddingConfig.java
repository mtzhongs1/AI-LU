package com.ailu.service.other.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
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
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

}
