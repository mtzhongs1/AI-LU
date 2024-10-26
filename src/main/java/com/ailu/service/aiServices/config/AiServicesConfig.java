package com.ailu.service.aiServices.config;

import com.ailu.properties.models.Zhipu;
import com.ailu.service.aiServices.ClassificationServices;
import com.ailu.service.aiServices.ComputeServices;
import com.ailu.service.aiServices.IChatServices;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.zhipu.ZhipuAiImageModel;
import dev.langchain4j.model.zhipu.ZhipuAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: ailu
 * @Date: 2024/10/25 下午3:38
 */

@Configuration
public class AiServicesConfig {
    @Bean
    public ClassificationServices classificationServices()
    {
        ChatLanguageModel model = Zhipu.buildChatLanguageModel(Zhipu.GLM_4_FLASH, 0.0);
        return AiServices.create(ClassificationServices.class, model);
    }
    @Bean
    public IChatServices chatServices() {
        ZhipuAiStreamingChatModel model = Zhipu.buildStreamingChatModel(Zhipu.GLM_4_FLASH, 0.9);
        return AiServices.create(IChatServices.class, model);
    }

    @Bean
    public ZhipuAiImageModel imageServices(){
        return Zhipu.buildImageModel(Zhipu.COGVIEW_3_PLUS);
    }

    @Bean
    public ComputeServices computeServices(){
        ZhipuAiStreamingChatModel model = Zhipu.buildStreamingChatModel(Zhipu.GLM_4_FLASH, 0.9);
        return AiServices.create(ComputeServices.class, model);
    }
}
