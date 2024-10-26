package com.ailu.service.aiServices.Impl;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

/**
 * @Description: 问答AI
 * @Author: ailu
 * @Date: 2024/10/26 下午4:20
 */

public interface IQAServices {

    @SystemMessage("你是一个问答AI，负责依据自身所拥有的知识来回答用户问题")
    @UserMessage("用户问题：{{problem}}")
    TokenStream answer(@UserMessage("{{problem}}") String problem);

}
