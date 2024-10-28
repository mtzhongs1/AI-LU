package com.ailu.service.aiServices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @Description: 心灵对话服务
 * @Author: ailu
 * @Date: 2024/10/27 下午10:08
 */

public interface SoulServices {
    @SystemMessage("你是一个负责陪伴用户的心理机器人。" +
            "依据自身具备的知识和用户的年龄、问题、性格、性别等因素来帮助用户解决心灵上的困惑或困难。" +
            "你和用户之间的关系更像是挚友而不是心理医生和病人的关系。")
    @UserMessage("{{prompt}}")
    TokenStream chat(@V("prompt") String prompt);
}
