package com.ailu.entity.graph;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * @Description: 知识图谱所需节点-实体
 * @Author: ailu
 * @Date: 2024/10/28 下午5:22
 */
@Description("实体和关系")
@Data
public class NodeAndEdge {

    //确定实体
    @Description("性格")
    private String character;
    @Description("性别")
    private String sex;
    @Description("年龄")
    private String age;
    @Description("学历")
    private String degree;
    @Description("经历")
    private String experience;
    @Description("用户目前的情绪状态")
    private String userMood;
    @Description("疾病")
    private String desease;
    @Description("疾病造成的症状或表现")
    private String symptom;


    // @Description("根据人目前的情绪状态、基本信息(性格、年龄、学历、经历)或疾病的症状,应该采取的对话方式,如:讲故事、鼓励式、引导式、角色扮演等")
    @Description("对话方式,如:讲故事、鼓励式、引导式、角色扮演等")
    private String dialogue;
    // @Description("根据人目前的情绪状态、基本信息(性格、年龄、学历、经历)或疾病的症状,应该展现给对方的对话情绪,如:积极、温柔等")
    // private String mood;
    @Description("药物")
    private String medicine;

    //定义关系，即三元组（实体，关系，实体）
    @Description("性格-采用-对话方式")
    private String characterDialogue;
    @Description("性别-采用-对话方式")
    private String sexDialogue;
    @Description("年龄-采用-对话方式")
    private String ageDialogue;
    @Description("学历-采用-对话方式")
    private String degreeDialogue;
    @Description("经历-采用-对话方式")
    private String experienceDialogue;
    @Description("症状-采用-对话方式")
    private String symptomDialogue;

    @Description("症状-采用-应该展现给对方的对话情绪")
    private String symptomMood;
    @Description("对方目前的情绪状态-采用-应该展现给对方的对话情绪")
    private String userMoodMood;
    @Description("症状-属于-疾病")
    private String symptomDesease;
    @Description("疾病-采用-药物")
    private String deseaseMedicine;



}
