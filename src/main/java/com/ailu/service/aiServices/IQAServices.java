package com.ailu.service.aiServices;

import dev.langchain4j.service.*;

/**
 * @Description: 问答AI
 * @Author: ailu
 * @Date: 2024/10/26 下午4:20
 */

public interface IQAServices {

    @SystemMessage("你是一个问答AI，负责依据自身所拥有的知识来回答用户问题")
    @UserMessage("用户问题：{{prompt}}")
    TokenStream answerQuestion(@MemoryId int memoryId, @V("prompt") String prompt);

}
