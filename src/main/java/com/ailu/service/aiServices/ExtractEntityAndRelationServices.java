package com.ailu.service.aiServices;

import com.ailu.entity.graph.NodeAndEdge;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @Description: 提取实体和关系的服务
 * @Author: ailu
 * @Date: 2024/10/28 下午8:29
 */

public interface ExtractEntityAndRelationServices {
    @SystemMessage(
            """
            ### 任务说明 ###
            你是一名知识图谱构建者，正在为一个“AI心理辅导”项目构建知识图谱。
            你的任务是从给定的文本中提取出实体(node)及其之间的关系(ralation)。
            
            ### 具体要求 ###
            1. 提取出的实体()和关系必须唯一，不得重复。
            2. 提取出的关系中的实体必须是nodes的成员。
            3. 每个关系记录包含主体（subject）、关系（relation）、和客体（object）。
            4. 确保输出符合以下json格式，且没有多余文本。
            
            输入示例：
            文本：<输入的文本内容>
            
            输出格式：
            {
                "nodes": ["实体名1", "实体名2", ... , "实体名n"],
                "relations": [
                    {"subject": "实体名1", "relation": "关系1", "object": "实体名2"},
                    {"subject": "实体名1", "relation": "关系2", "object": "实体名3"},
                    {"subject": "实体名2", "relation": "关系3", "object": "实体名3"},
                    ...
                    {"subject": "实体名n", "relation": "关系k", "object": "实体名m"}
                ]
            }
            
            示例：
            输入：
            文本：对待患有抑郁症的人，AI应该表示理解、关爱他们
            输出：
            {
                "nodes": ["抑郁症","理解关爱",“AI”],
                "relations": [
                    {"subject": “AI”, "relation": "理解关爱", "object": "抑郁症"},
                ]
            }
            """)
    @UserMessage("文本：{{prompt}}")
    String extractNodeAndRelation(@V("prompt") String prompt);
}
