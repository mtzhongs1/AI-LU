package com.ailu.service.aiServices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @Description: 知识问答
 * @Author: ailu
 * @Date: 2024/10/26 下午12:37
 */

public interface QAServices {
    @SystemMessage("你是一个问答，负责和用户进行日常对话")
    @UserMessage("{{prompt}}")
    String chat(@V("prompt") String prompt);
}
