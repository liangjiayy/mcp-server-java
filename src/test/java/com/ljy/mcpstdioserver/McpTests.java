package com.ljy.mcpstdioserver;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @author liang
 * @description 用于快速测试Mcp工具
 */
public class McpTests {
    private static McpSyncClient client;
    @BeforeAll
    public static void init() {
        var stdioParams = ServerParameters.builder("java")
                .args("-jar",
                        "-Dspring.ai.mcp.server.stdio=true",
                        "D:\\SourceCode\\IdeaProjects\\mcp-weather-stdio-server\\target\\mcp-server-java-0.0.1-SNAPSHOT.jar")
                .build();

        var transport = new StdioClientTransport(stdioParams);
        client = McpClient.sync(transport).build();

        client.initialize();
    }

    @Test
    public void testDb() {
        var result = client.callTool(new McpSchema.CallToolRequest("executeSql",
                Map.of(
                        "sqls", List.of(
                                "select * from user",
                                "insert into test.user (username, password) values ('aaa','bbb')"
                        ),
                        "dbId", "mysql1"
                )));
        System.out.println(result);

    }
}
