package com.ailu.util;

import dev.langchain4j.service.TokenStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * @Description:
 * @Author: ailu
 * @Date: 2024/10/26 下午5:58
 */
@Slf4j
public class TokenStreamUtil {
    public static void toSseEmitter(TokenStream tokenStream,SseEmitter sseEmitter){
        tokenStream
                .onNext((token) -> {
                    try {
                        sseEmitter.send(token);
                    } catch (IOException e) {
                        log.error("stream onNext error", e);
                    }
                })
                .onComplete((response) -> {
                    log.info("返回数据结束了:{}", response);
                    sseEmitter.complete();
                })
                .onError((error) -> {
                    log.error("stream error", error);
                })
                .start();
    }
}
