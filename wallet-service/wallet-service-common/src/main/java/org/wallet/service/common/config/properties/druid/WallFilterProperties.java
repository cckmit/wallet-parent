package org.wallet.service.common.config.properties.druid;

import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.wall.WallConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.datasource.filter.wall")
public class WallFilterProperties {
    private String dbType = JdbcUtils.MYSQL;
    private WallConfig config = new WallConfig(){{
        setAlterTableAllow(false);
        setTruncateAllow(false);
        setDropTableAllow(false);
        setNoneBaseStatementAllow(false);
        setUpdateWhereNoneCheck(true);
        setSelectIntoOutfileAllow(false);
        setMergeAllow(true);
    }};
    private volatile boolean logViolation = true;
    private volatile boolean throwException = true;
}
