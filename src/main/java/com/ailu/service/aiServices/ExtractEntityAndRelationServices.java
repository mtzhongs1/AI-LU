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
            你是一名知识图谱构建者，正为一个“AI心理辅导”做一个知识图谱
            你的任务是从给定的文本中提取实体及其之间的关系，注意实体和关系不能重复。
            """)
    @UserMessage("文本:{{prompt}}")
    NodeAndEdge extractNodeAndRelation(@V("prompt") String prompt);
}
