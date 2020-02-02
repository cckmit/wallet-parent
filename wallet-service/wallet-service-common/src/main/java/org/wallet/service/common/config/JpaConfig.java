package org.wallet.service.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.wallet.service.common.dao.BaseRepositoryFactoryBean;

/**
 * JPA 配置
 * @author zengfucheng
 **/
@Slf4j
@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = "org.wallet.common.entity")
@EnableJpaRepositories(basePackages = "org.wallet.service.*.dao", repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
public class JpaConfig {

    @Bean
    public JpaProperties jpaProperties(){
        log.info("load JPA Properties");
        JpaProperties jpaProperties = new JpaProperties();
        jpaProperties.setShowSql(true);
        jpaProperties.setOpenInView(false);
        jpaProperties.setDatabasePlatform("org.wallet.service.common.mysql.MySQL5DialectUTF8");
        return jpaProperties;
    }

}
