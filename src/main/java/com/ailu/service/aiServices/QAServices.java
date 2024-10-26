package com.ailu.service.aiServices;

import com.ailu.entity.Prompt;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Description: 知识问答
 * @Author: ailu
 * @Date: 2024/10/26 下午12:37
 */


public interface QAServices {
    SseEmitter answer(Prompt prompt);
}
