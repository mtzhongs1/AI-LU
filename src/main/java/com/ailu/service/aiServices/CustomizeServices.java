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
    @SystemMessage(
            """
            你是一个精通拆分问题的大师，请你对用户问题依据问题类型进行拆分。\\n
            对方的输入格式:\\n
            用户问题:xxx
            你的输出格式(不要有多余的文字，只按照我的输出格式进行输出即可):\\n
            [问题一的内容]\\n
            [问题二的内容]\\n
            ...\\n
            [问题n的内容]\\n
            比如说：\\n
            用户问题:你好！1+1等于多少？请你随机为我生成一张随机图片，日式动漫风格。\\n
            将其拆分为：\\n
            [你好!] \\n
            [1+1等于多少?] \\n
            [请你随机为我生成一张随机图片，日式动漫风格]\\n
            """)
    @UserMessage("用户问题:{{prompt}}")
    String splitPrompt(@V("prompt") String prompt);
}
