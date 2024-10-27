package com.ailu.service.agent;

import com.ailu.common.Result;
import com.ailu.entity.Prompt;
import com.ailu.properties.enums.ContentCategoryEnum;
import com.ailu.properties.models.Zhipu;
import com.ailu.service.aiServices.*;
import com.ailu.service.aiServices.Impl.IChatServicesImpl;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.zhipu.ZhipuAiImageModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Agent接口
 * @Author: ailu
 * @Date: 2024/10/25 下午12:56
 */

@Service
@AllArgsConstructor
public class AgentService {
    @Autowired
    private CustomizeServices customizeServices;

    @Autowired
    private ClassificationServices classification;

    @Autowired
    private IChatServicesImpl iChatServicesImpl;

    @Autowired
    private ZhipuAiImageModel zhipuAiImageModel;

    @Autowired
    private QAServices qaServices;

    @Autowired
    private ComputeServices computeServices;

    public Result agent(Prompt prompt){
        //切分用户问题
        List<String> promptSegs = customizeServices.splitPrompt(prompt.getPrompt());

        List<Object> results = new ArrayList<>();
        for (String promptSeg : promptSegs) {
            //获取用户问题分类
            ContentCategoryEnum category = classification.classify(promptSeg);
            //根据分类执行不同的AiServices
            Object result = switch (category) {
                case CHAT -> iChatServicesImpl.chat(promptSeg);
                case IMAGE_GENERATION -> zhipuAiImageModel.generate(promptSeg).content().url();
                case COMPUTE -> computeServices.compute(promptSeg);
                case ANSWER_QUESTIONS -> {
                    Prompt p = new Prompt(promptSeg, prompt.getKnowledgeBaseUuid());
                    yield qaServices.answer(p);
                }
            };
            results.add(result);
        }
        return Result.success(results);
    }
}
