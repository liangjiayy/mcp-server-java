package com.ljy.mcpstdioserver.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liang
 * @description 示例 - resource
 */
@Service
public class MyResource {
    @Bean
    public List<McpServerFeatures.SyncResourceSpecification> myResources() {
        //public Resource(@JsonProperty("uri") String uri, @JsonProperty("name") String name, @JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("mimeType") String mimeType, @JsonProperty("size") Long size, @JsonProperty("annotations") Annotations annotations, @JsonProperty("_meta") Map<String, Object> meta)
        var systemInfoResource = new McpSchema.Resource("system-info", "system-info", "system-info", "system-info", "application/json", 1000L, null, null);
        var resourceSpecification = new McpServerFeatures.SyncResourceSpecification(systemInfoResource, (exchange, request) -> {
            try {
                var systemInfo = Map.of("systemInfo", "systemInfo");
                String jsonContent = new ObjectMapper().writeValueAsString(systemInfo);
                return new McpSchema.ReadResourceResult(
                        List.of(new McpSchema.TextResourceContents(request.uri(), "application/json", jsonContent)));
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to generate system info", e);
            }
        });

        return List.of(resourceSpecification);
    }
}
