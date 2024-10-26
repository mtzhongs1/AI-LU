package com.ailu.service.agent;

import com.ailu.common.Result;
import com.ailu.entity.Prompt;
import com.ailu.properties.enums.ContentCategoryEnum;
import com.ailu.properties.models.Zhipu;
import com.ailu.service.aiServices.ClassificationServices;
import com.ailu.service.aiServices.ComputeServices;
import com.ailu.service.aiServices.IChatServices;
import com.ailu.service.aiServices.QAServices;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.zhipu.ZhipuAiImageModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: Agent接口
 * @Author: ailu
 * @Date: 2024/10/25 下午12:56
 */

@Service
@AllArgsConstructor
public class AgentService {
    @Autowired
    private ClassificationServices classification;

    @Autowired
    private IChatServices chat;

    @Autowired
    private ZhipuAiImageModel zhipuAiImageModel;

    @Autowired
    private QAServices qaServices;

    @Autowired
    private ComputeServices computeServices;

    public Result agent(Prompt prompt){
        ContentCategoryEnum category = classification.classify(prompt.getPrompt());
        Result result = switch (category) {
            case CHAT -> Result.success(chat.chat(prompt.getPrompt()));
            case IMAGE_GENERATION -> Result.success(zhipuAiImageModel.generate(prompt.getPrompt()).content().url());
            case ANSWER_QUESTIONS -> Result.success(qaServices.answer(prompt));
            case COMPUTE -> Result.success(computeServices.compute(prompt.getPrompt()));
        };
        return result;
    }
}
