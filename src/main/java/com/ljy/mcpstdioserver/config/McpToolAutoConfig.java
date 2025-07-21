package com.ljy.mcpstdioserver.config;

import com.ljy.mcpstdioserver.config.anno.McpTool;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liang
 * @description 自动配置McpTool
 */
@Configuration
public class McpToolAutoConfig {

    @Bean
    public MethodToolCallbackProvider methodToolCallbackProvider(ApplicationContext applicationContext){
        Object[] array = applicationContext.getBeansWithAnnotation(McpTool.class).values().toArray();
        return MethodToolCallbackProvider.builder().toolObjects(array).build();
    }
}
