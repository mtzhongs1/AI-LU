package com.ailu.entity.graph;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;
/**
 * @Description: 用户的描述
 * @Author: ailu
 * @Date: 2024/10/28 下午1:59
 */
@Data
public class User {
    @Description("姓名")
    private String name;
    @Description("性格")
    private String character;
    @Description("性别")
    private String sex;
    @Description("年龄")
    private String age;
    @Description("学历")
    private String degree;
    @Description("目前的情绪状态")
    private String mood;
    @Description("自我总结")
    private String description;
    @Description("心理疾病类型")
    private String diseaseType;
}
