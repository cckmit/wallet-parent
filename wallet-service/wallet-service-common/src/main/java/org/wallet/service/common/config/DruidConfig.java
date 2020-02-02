package org.wallet.service.common.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.wall.WallFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wallet.service.common.config.properties.druid.DataSourceProperties;
import org.wallet.service.common.config.properties.druid.StatFilterProperties;
import org.wallet.service.common.config.properties.druid.StatViewServletProperties;
import org.wallet.service.common.config.properties.druid.WallFilterProperties;

import javax.sql.DataSource;

/**
 * Druid 配置
 * @author zengfucheng
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(value = "spring.datasource.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({
        DataSourceProperties.class,
        WallFilterProperties.class,
        StatFilterProperties.class,
        StatViewServletProperties.class
})
public class DruidConfig {

    @Bean
    public DataSource druidDataSource(DataSourceProperties dataSourceProperties,
                                      StatFilterProperties statFilterProperties,
                                      WallFilterProperties wallFilterProperties) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSourceProperties.config(dataSource);

        dataSource.clearFilters();

        StatFilter statFilter = new StatFilter();
        BeanUtils.copyProperties(statFilterProperties, statFilter);

        WallFilter wallFilter = new WallFilter();
        BeanUtils.copyProperties(wallFilterProperties, wallFilter);

        dataSource.getProxyFilters().add(statFilter);
        dataSource.getProxyFilters().add(wallFilter);

        log.info("datasource.url: {}", dataSource.getUrl());
        log.info("datasource.username: {}", dataSource.getUsername());
        log.info("datasource.password: {}", dataSource.getPassword());

        return dataSource;
    }

    @Bean
    public ServletRegistrationBean druidServletRegistration(StatViewServletProperties statViewServletProperties) {
        ServletRegistrationBean<StatViewServlet> registration = new ServletRegistrationBean<>(new StatViewServlet());
        registration.addUrlMappings(statViewServletProperties.getUrlPattern());
        // 添加IP白名单
        registration.addInitParameter("allow", statViewServletProperties.getAllow());
        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
        registration.addInitParameter("deny", statViewServletProperties.getDeny());
        // 添加控制台管理用户
        registration.addInitParameter("loginUsername", statViewServletProperties.getLoginUsername());
        registration.addInitParameter("loginPassword", statViewServletProperties.getLoginPassword());
        // 是否能够重置数据
        registration.addInitParameter("resetEnable", statViewServletProperties.getResetEnable());

        log.info("druid.stat.url: {}", statViewServletProperties.getUrlPattern());
        log.info("druid.stat.username: {}", statViewServletProperties.getLoginUsername());
        log.info("druid.stat.password: {}", statViewServletProperties.getLoginPassword());

        return registration;
    }
}
