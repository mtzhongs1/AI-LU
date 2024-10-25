package com.ailu.ailu.properties.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Description: 对用户问题进行内容分类
 * @Author: ailu
 * @Date: 2024/10/25 上午10:53
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ContentCategoryEnum {
    CHAT(1,"聊天"),
    ANSWER_QUESTIONS(2,"回答问题"),
    TRANSACTION(3, "翻译"),
    TEXT_REFINE(4,"文本润色"),
    IMAGE_GENERATION(5, "图片生成"),
    COMPUTE(6,"计算");

    private Integer type;
    private String description;
}
