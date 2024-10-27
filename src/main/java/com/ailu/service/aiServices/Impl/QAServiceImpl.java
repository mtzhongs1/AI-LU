package com.ailu.service.aiServices.Impl;

import com.ailu.entity.Prompt;
import com.ailu.properties.models.Zhipu;
import com.ailu.service.aiServices.IQAServices;
import com.ailu.service.aiServices.QAServices;
import com.ailu.util.TokenStreamUtil;
import com.google.common.collect.ImmutableMap;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

/**
 * @Description: 问答接口
 * @Author: ailu
 * @Date: 2024/10/26 下午2:27
 */
@Service
@Slf4j
public class QAServiceImpl implements QAServices {

    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;
    @Autowired
    private EmbeddingModel embeddingModel;
    @Autowired
    private ChatMemoryStore chatMemoryStore;
    @Resource(name = "freeThreadPoolExecutor")
    private ThreadPoolExecutor freeThreadPoolExecutor;

    @Override
    public SseEmitter answer(Prompt prompt) {

        //如果知识库id为null,则直接回答问题
        if(prompt.getKnowledgeBaseUuid() == null){
            IQAServices iqaServices = AiServices.builder(IQAServices.class)
                    .streamingChatLanguageModel(Zhipu.buildStreamingChatModel(Zhipu.GLM_4_FLASH, 0.2))
                    .build();
            TokenStream tokenStream = iqaServices.answerQuestion(1,prompt.getPrompt());
            SseEmitter sseEmitter = new SseEmitter();
            TokenStreamUtil.toSseEmitter(tokenStream,sseEmitter);
            return sseEmitter;
        }

        //内容检索器
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                // 根据query动态指定maxResults，这里固定返回3
                .dynamicMaxResults(query -> 3)
                .minScore(0.75)
                // 根据query动态指定minScore，这里固定返回0.75
                .dynamicMinScore(query -> 0.75)
                .filter(metadataKey("uuid").isEqualTo(prompt.getKnowledgeBaseUuid()))
                        .build();

        // 创建聊天记忆提供者，用于管理聊天历史记录
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(6)
                .chatMemoryStore(chatMemoryStore)
                .build();

        // 创建检索增强器，用于增强提示生成过程中的内容检索,作为 RAG 流程的入口点
        DefaultRetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .executor(freeThreadPoolExecutor)
                .queryTransformer(new CompressingQueryTransformer(Zhipu.buildChatLanguageModel(Zhipu.GLM_4_FLASH, 0.0)))
                .build();

        //构建聊天助理，包括流式聊天语言模型、检索增强器和聊天记忆提供者
        IQAServices iqaServices = AiServices.builder(IQAServices.class)
                .streamingChatLanguageModel(Zhipu.buildStreamingChatModel(Zhipu.GLM_4_FLASH, 0.2))
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemoryProvider(chatMemoryProvider)
                .build();

        //流式响应,memoryId暂且给1，实际可设为用户id
        TokenStream tokenStream = iqaServices.answerQuestion(1,prompt.getPrompt());
        SseEmitter sseEmitter = new SseEmitter();
        TokenStreamUtil.toSseEmitter(tokenStream,sseEmitter);
        return sseEmitter;
    }
}
