package com.ailu.service.aiServices;

import com.ailu.properties.enums.ContentCategoryEnum;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

/**
 * @Description: AI分类用户内容接口
 * @Author: ailu
 * @Date: 2024/10/25 上午11:06
 */

public interface ClassificationServices {
    @SystemMessage("你是一个负责分类用户内容的助理")
    @UserMessage("用户内容：{{prompt}}")
    ContentCategoryEnum classify(@V("prompt") String prompt);
}
