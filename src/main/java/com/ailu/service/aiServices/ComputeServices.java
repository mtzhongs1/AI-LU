package com.ailu.service.aiServices;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @author: ailu
 * @description: 计算服务
 * @date: 2024/10/26 下午4:44
 */

public interface ComputeServices {
    @SystemMessage("你是一个计算机器，负责帮用户计算运算式")
    @UserMessage("{{prompt}}")
    String compute(@V("prompt") String prompt);
}
