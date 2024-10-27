package com.ailu.service.aiServices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

/**
 * @author: ailu
 * @description: 拆分用户prompt
 * @date: 2024/10/27 下午1:53
 */

public interface CustomizeServices {
    @SystemMessage("你是一个精通拆分问题的大师，请你对用户问题依据问题类型进行拆分。" +
            "比如说：用户的问题是：你好！1+1等于多少？请你随机为我生成一张随机图片，日式动漫风格。" +
            "将其拆分为：" +
            "(1) 你好!" +
            "(2) 1+1等于多少？" +
            "(3) 请你随机为我生成一张随机图片，日式动漫风格")
    @UserMessage("用户问题:{{prompt}}")
    List<String> splitPrompt(@V("prompt") String prompt);
}
