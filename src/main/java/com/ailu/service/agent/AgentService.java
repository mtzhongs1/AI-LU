package com.ailu.service.agent;

import com.ailu.common.Result;
import com.ailu.entity.Prompt;
import com.ailu.properties.enums.ContentCategoryEnum;
import com.ailu.service.aiServices.*;
import com.ailu.service.aiServices.enhance.IChatServicesEnhance;
import com.ailu.service.aiServices.enhance.QAServicesEnhance;
import dev.langchain4j.model.zhipu.ZhipuAiImageModel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private IChatServicesEnhance iChatServicesEnhance;

    @Autowired
    private ZhipuAiImageModel zhipuAiImageModel;

    @Autowired
    private QAServicesEnhance qaServicesEnhance;

    @Autowired
    private ComputeServices computeServices;

    public Result agent(Prompt prompt){
        //切分用户问题，因为智谱AI不提供json格式，所以没办法返回List<String>
        String promptSegsStr = customizeServices.splitPrompt(prompt.getPrompt());
        List<String> promptSegs = extractBracketContents(promptSegsStr);

        List<Object> results = new ArrayList<>();
        for (String promptSeg : promptSegs) {
            //获取用户问题分类
            ContentCategoryEnum category = classification.classify(promptSeg);
            //根据分类执行不同的AiServices
            Object result = switch (category) {
                case CHAT -> iChatServicesEnhance.chat(promptSeg);
                case IMAGE_GENERATION -> zhipuAiImageModel.generate(promptSeg).content().url();
                case COMPUTE -> computeServices.compute(promptSeg);
                case ANSWER_QUESTIONS -> {
                    Prompt p = new Prompt(promptSeg, prompt.getKnowledgeBaseUuid());
                    yield qaServicesEnhance.answer(p);
                }
                case SOULFUL_DIALOGUE -> null;
            };
            results.add(result);
        }
        return Result.success(results);
    }

    public static List<String> extractBracketContents(String input) {
        // 定义正则表达式来匹配 [] 内的内容
        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // 创建一个列表来存储结果
        List<String> result = new ArrayList<>();

        // 查找所有匹配项并添加到列表中
        while (matcher.find()) {
            result.add(matcher.group(1)); // group(1) 获取第一个捕获组，即 [] 内的内容
        }

        return result;
    }
}
