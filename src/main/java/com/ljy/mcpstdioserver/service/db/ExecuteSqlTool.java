package com.ljy.mcpstdioserver.service.db;

import com.ljy.mcpstdioserver.config.anno.McpTool;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.ExplainStatement;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@McpTool
public class ExecuteSqlTool {

    private final DatabaseProperties databaseProperties;
    private final Map<String, DataSource> dataSourceMap;

    public ExecuteSqlTool(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
        this.dataSourceMap = new HashMap<>();

        databaseProperties.getDatasources().stream().forEach(config -> {
            String driverClassName = null;
            switch (config.getType()) {
                case "mysql":
                    driverClassName = com.mysql.cj.jdbc.Driver.class.getName();
                    break;
                // case "postgresql":
                //     driverClassName = "org.postgresql.Driver";
                //     break;
                // case "oracle":
                //     driverClassName = "oracle.jdbc.driver.OracleDriver";
                //     break;
                // case "sqlserver":
                //     driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                //     break;
                default:
                    // throw new IllegalArgumentException("Unsupported database type: " + config.getType());
                    return;
            }

            DataSource datasource = DataSourceBuilder.create()
                    .driverClassName(driverClassName)
                    .url(config.getUrl())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .build();
            dataSourceMap.put(config.getId(), datasource);
        });
    }

    @Tool(description = "执行SQL语句")
    public Map<String, Object> executeSql(@ToolParam(description = "数据库 ID") String dbId,
                                          @ToolParam(description = "SQL 语句") List<String> sqls) {
        Map<String, Object> response = new HashMap<>();

        try {
            DatabaseProperties.DatabaseConfig dbConfig = databaseProperties.getDatasources().stream()
                    .filter(config -> config.getId().equals(dbId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid database ID"));

            DataSource dataSource = dataSourceMap.get(dbId);
            if (dataSource == null) {
                throw new IllegalStateException("No DataSource configured for this database ID");
            }

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceMap.get(dbId));

            long startTime = System.currentTimeMillis();
            List<Object> results = new ArrayList<>();

            sqls.forEach(sql -> {
                sql = sql.trim();

                // 添加结果的方法
                String finalSql = sql;
                Consumer addResult = (r) -> {
                    results.add(Map.of("sqlPrefix", finalSql.substring(0, Math.min(20, finalSql.length())), "result", r));
                };
                // 执行sql
                try {
                    Statement statement = getStatement(sql);
                    boolean isReadOnlyQuery = statement instanceof Select || statement instanceof ExplainStatement;
                    if (dbConfig.getSqlExecutionType() == DatabaseProperties.SqlExecutionType.READ_ONLY &&
                            !isReadOnlyQuery) {
                        throw new SecurityException("Read-only database cannot execute write operations");
                    }

                    if (isReadOnlyQuery) {
                        // 查询操作
                        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
                        addResult.accept(result);
                    } else {
                        // 修改操作
                        int affectedRows = jdbcTemplate.update(sql);
                        Map<String, Object> updateResult = new HashMap<>();
                        updateResult.put("affectedRows", affectedRows);
                        addResult.accept(updateResult);
                    }
                } catch (Exception e) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("error", "SQL execution failed: " + e.getMessage());
                    addResult.accept(List.of(errorResult));
                }
            });
            long executionTime = System.currentTimeMillis() - startTime;

            response.put("result", results);
            response.put("executionTimeMs", executionTime);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }

    private Statement getStatement(String sql) throws JSQLParserException {
        Statement stmt = CCJSqlParserUtil.parse(sql);
        return stmt;
    }
}
