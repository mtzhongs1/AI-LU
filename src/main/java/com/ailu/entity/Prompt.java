package com.ailu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 用户输入内容
 * @Author: ailu
 * @Date: 2024/10/25 下午2:42
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prompt {
    private String prompt;
    private String knowledgeBaseUuid;
}
