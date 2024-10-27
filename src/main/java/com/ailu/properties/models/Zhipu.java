package com.ailu.properties.models;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiImageModel;
import dev.langchain4j.model.zhipu.ZhipuAiStreamingChatModel;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @Description: 构建质谱模型
 * @Author: ailu
 * @Date: 2024/10/25 下午1:02
 */
@Service
public class Zhipu {
    public static final String GLM_4_FLASH = "glm-4-flash";
    public static final String COGVIEW_3_PLUS = "cogview-3-plus";
    public static final String KEY = "xxx";

    private static final Duration TIME_OUT = Duration.of(60000, ChronoUnit.MILLIS);
    private static final String BASE_URL = "https://open.bigmodel.cn/";

    public static ChatLanguageModel buildChatLanguageModel(String model,double temperature){
        return ZhipuAiChatModel.builder()
                .apiKey(KEY)
                .model(model)
                .connectTimeout(TIME_OUT)
                .callTimeout(TIME_OUT)
                .readTimeout(TIME_OUT)
                .writeTimeout(TIME_OUT)
                .baseUrl(BASE_URL)
                .temperature(temperature)
                .logResponses(true)
                .logRequests(true)
                .maxRetries(1)
                .build();
    }

    public static ZhipuAiStreamingChatModel buildStreamingChatModel(String model,double temperature){
        return ZhipuAiStreamingChatModel.builder()
                .apiKey(KEY)
                .model(model)
                .connectTimeout(TIME_OUT)
                .callTimeout(TIME_OUT)
                .readTimeout(TIME_OUT)
                .writeTimeout(TIME_OUT)
                .baseUrl(BASE_URL)
                .temperature(temperature)
                .logResponses(true)
                .logRequests(true)
                .build();
    }

    public static ZhipuAiImageModel buildImageModel(String model){
        return ZhipuAiImageModel.builder()
                .apiKey(KEY)
                .model(model)
                .connectTimeout(TIME_OUT)
                .callTimeout(TIME_OUT)
                .readTimeout(TIME_OUT)
                .writeTimeout(TIME_OUT)
                .baseUrl(BASE_URL)
                .logResponses(true)
                .logRequests(true)
                .maxRetries(1)
                .build();
    }
}
