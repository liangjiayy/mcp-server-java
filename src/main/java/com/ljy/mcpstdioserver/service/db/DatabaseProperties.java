package com.ljy.mcpstdioserver.service.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "mcp.dbs")
public class DatabaseProperties {

    private List<DatabaseConfig> datasources;

    @Data
    public static class DatabaseConfig {
        private String id;
        private String description;
        private String type;
        private String url;
        private String username;
        private String password;
        private SqlExecutionType sqlExecutionType;
    }

    public enum SqlExecutionType {
        READ_ONLY, READ_WRITE
    }

    public List<DatabaseConfig> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<DatabaseConfig> datasources) {
        this.datasources = datasources;
    }
}
