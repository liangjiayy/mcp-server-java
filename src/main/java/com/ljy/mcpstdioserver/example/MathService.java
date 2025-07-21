package com.ljy.mcpstdioserver.example;

import com.ljy.mcpstdioserver.config.anno.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;

/**
 * @author liang
 * @description 示例 - tool
 */
@McpTool
public class MathService {
    @Tool(description = "对两个数字求和")
    public String addMethod(@ToolParam(description = "第一个数字") String a, @ToolParam(description = "第二个数字") String b) {
        BigDecimal a1 = new BigDecimal(a);
        BigDecimal b1 = new BigDecimal(b);
        return a1.add(b1).toString();
    }

    public static void main(String[] args) {
        System.out.println(new MathService().addMethod("1", "2"));
    }
}
