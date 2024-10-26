package com.ailu.controller.agent;

import com.ailu.common.Result;
import com.ailu.entity.Prompt;
import com.ailu.service.agent.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: ailu
 * @Date: 2024/10/25 下午12:49
 */

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping
    public Result agent(Prompt prompt) {
        return agentService.agent(prompt);
    }
}
