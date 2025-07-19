package com.ljy.mcpstdioserver.config.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author liang
 * @description 用于指定类为McpTool
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface McpTool {
}
