package com.ailu.controller;

import com.ailu.common.Result;
import com.ailu.service.other.KnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 知识库Controller
 * @Author: ailu
 * @Date: 2024/10/26 下午12:46
 */

@RestController
@RequestMapping("/knowledgeBase")
public class KnowledgeBaseController {
    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    /**
     * 知识库
     */
    @PostMapping("/upload")
    public Result uploadKnowledgeBase(MultipartFile file) {
        knowledgeBaseService.uploadKnowledgeBase(file);
        return Result.success();
    }

    /**
     * 知识图谱
     */
    @PostMapping("/upload/graph")
    public Result uploadKnowledgeGraph(MultipartFile file) {
        knowledgeBaseService.uploadKnowledgeGraph(file);
        return Result.success();
    }
}
