package com.ljy.mcpstdioserver;

import com.ljy.mcpstdioserver.service.db.ExecuteSqlTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class McpWeatherStdioServerApplicationTests {
    @Autowired
    ExecuteSqlTool executeSqlTool;

    @Test
    void contextLoads() {
        System.out.println(executeSqlTool.executeSql("mysql1", List.of("explain analyze select * from user")));
    }
}
