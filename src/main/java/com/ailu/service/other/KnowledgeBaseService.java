package com.ailu.service.other;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: ailu
 * @description: 知识库相关操作
 * @date: 2024/10/26 下午12:39
 */

public interface KnowledgeBaseService {
    void uploadKnowledgeBase(MultipartFile file);

    void uploadKnowledgeGraph(MultipartFile file);
}
