package com.ailu.properties.enums;

import dev.langchain4j.model.output.structured.Description;
import lombok.*;

/**
 * @Description: 对用户问题进行内容分类
 * @Author: ailu
 * @Date: 2024/10/25 上午10:53
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ContentCategoryEnum {
    @Description("日常闲聊")
    CHAT(1,"日常闲聊"),
    @Description("知识问题")
    ANSWER_QUESTIONS(2,"知识问题"),
    @Description("图片生成")
    IMAGE_GENERATION(3, "图片生成"),
    @Description("计算运算式")
    COMPUTE(4,"计算运算式"),
    @Description("心灵对话，用户的对话带有情绪色彩时")
    SOULFUL_DIALOGUE(5,"心灵对话");

    ContentCategoryEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    private Integer type;
    private String description;
    @Setter
    private String prompt;
}
