package com.ljy.mcpstdioserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author liang
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class McpStdioServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpStdioServerApplication.class, args);
    }
}
