package com.ljy.mcpstdioserver.config;

import com.ljy.mcpstdioserver.config.anno.McpTool;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author liang
 * @description 自动配置McpTool
 */
@Configuration
public class McpToolAutoConfig implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(McpTool.class);

        // 自动配置McpTool
        MethodToolCallbackProvider build = MethodToolCallbackProvider.builder().toolObjects(beansWithAnnotation.values().toArray()).build();
        beanFactory.registerSingleton(build.getClass().getName(), build);
    }
}
