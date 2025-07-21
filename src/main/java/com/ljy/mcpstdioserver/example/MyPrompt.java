package com.ljy.mcpstdioserver.example;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liang
 * @description 示例 - prompt
 */
@Service
public class MyPrompt {
    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> myPrompts() {
        var prompt = new McpSchema.Prompt("greeting", "A friendly greeting prompt",
                List.of(new McpSchema.PromptArgument("name", "The name to greet", true)));

        var promptSpecification = new McpServerFeatures.SyncPromptSpecification(prompt, (exchange, getPromptRequest) -> {
            String nameArgument = (String) getPromptRequest.arguments().get("name");
            if (nameArgument == null) { nameArgument = "friend"; }
            var userMessage = new McpSchema.PromptMessage(McpSchema.Role.USER, new McpSchema.TextContent("Hello " + nameArgument + "! How can I assist you today?"));
            return new McpSchema.GetPromptResult("A personalized greeting message", List.of(userMessage));
        });

        return List.of(promptSpecification);
    }
}
