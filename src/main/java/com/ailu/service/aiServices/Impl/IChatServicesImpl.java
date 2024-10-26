package com.ailu.service.aiServices.Impl;

import com.ailu.service.aiServices.IChatServices;
import com.ailu.util.TokenStreamUtil;
import dev.langchain4j.service.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * @Description:
 * @Author: ailu
 * @Date: 2024/10/26 下午5:55
 */

@Service
public class IChatServicesImpl {
    @Autowired
    private IChatServices chat;

    public SseEmitter chat(String prompt){
        TokenStream tokenStream = chat.chat(prompt);
        SseEmitter sseEmitter = new SseEmitter();
        TokenStreamUtil.toSseEmitter(tokenStream,sseEmitter);
        return sseEmitter;
    }

}
