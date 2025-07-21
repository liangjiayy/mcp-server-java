package com.ljy.mcpstdioserver.service.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DbResource {
    private final DatabaseProperties databaseProperties;

    public DbResource(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @Bean
    public List<McpServerFeatures.SyncResourceSpecification> dbResources() {
        return databaseProperties.getDatasources().stream().map(dbConfig -> {
            String uri = dbConfig.getType() + "://" + dbConfig.getId();
            String description = dbConfig.getDescription() + " - " + dbConfig.getSqlExecutionType();
            var resource = new McpSchema.Resource(
                    uri, dbConfig.getId(), dbConfig.getDescription(), description,
                    "application/json", 1000L, null, null);
            return new McpServerFeatures.SyncResourceSpecification(resource, (exchange, request) -> {
                try {
                    var info = Map.of(
                            "id", dbConfig.getId(),
                            "description", dbConfig.getDescription(),
                            "sqlExecutionType", dbConfig.getSqlExecutionType().toString());
                    String jsonContent = new ObjectMapper().writeValueAsString(info);
                    return new McpSchema.ReadResourceResult(
                            List.of(new McpSchema.TextResourceContents(request.uri(), "application/json",
                                    jsonContent)));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to generate resource information", e);
                }
            });
        }).collect(Collectors.toList());
    }
}
