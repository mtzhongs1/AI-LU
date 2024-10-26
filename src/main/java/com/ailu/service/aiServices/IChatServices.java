package com.ailu.service.aiServices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @author: ailu
 * @description: AI聊天接口
 * @date: 2024/10/25 下午2:56
 */

public interface IChatServices {
    @SystemMessage("你是一个聊天机器人，负责和用户进行日常对话")
    @UserMessage("{{prompt}}")
    String chat(@V("prompt") String prompt);
}
