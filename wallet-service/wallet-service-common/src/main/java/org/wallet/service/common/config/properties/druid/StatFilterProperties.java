package org.wallet.service.common.config.properties.druid;

import com.alibaba.druid.util.JdbcUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.datasource.filter.stat")
public class StatFilterProperties {
    private long slowSqlMillis = 3 * 1000;
    private boolean logSlowSql = false;
    private String dbType = JdbcUtils.MYSQL;
    private boolean mergeSql = false;
}
