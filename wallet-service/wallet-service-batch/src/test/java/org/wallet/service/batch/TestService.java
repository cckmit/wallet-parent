package org.wallet.service.batch;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ISequence;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Slf4j
@Transactional(timeout = 3)
public class TestService extends SpringBootJUnitTest {

    @Reference(group = DubboServiceGroup.DAP_SEQUENCE, check = false)
    private ISequence sequence;

    @Reference(group = DubboServiceGroup.CLIENT_BIBOX, check = false)
    private IService biBoxService;

    @Reference(group = DubboServiceGroup.CLIENT_BIT_Z, timeout = 10000, check = false)
    private IService bitZService;

    @Resource
    DataSource dataSource;

    @Autowired
    Cache cache;

    private String setName = "SET:CURRENT";

    @Test
    public void contextLoads() throws SQLException {

        System.out.println("数据源>>>>>>" + dataSource.getClass());
        Connection connection = dataSource.getConnection();

        System.out.println("连接>>>>>>>>>" + connection.getClass().getName());
        System.out.println("连接地址>>>>>" + connection.getMetaData().getURL());

        DruidDataSource druidDataSource = (DruidDataSource) dataSource;

        System.out.println("DruidDataSource 数据源最大连接数：" + druidDataSource.getMaxActive());
        System.out.println("DruidDataSource 数据源初始化连接数：" + druidDataSource.getInitialSize());

        List<Filter> filters = druidDataSource.getProxyFilters();

        for (Filter filter : filters) {
            if(filter instanceof WallFilter){
                WallFilter wallFilter = (WallFilter) filter;
                System.out.println("WallFilter db-type：" + wallFilter.getDbType());
                System.out.println("WallFilter log-violation：" + wallFilter.isLogViolation());
                System.out.println("WallFilter throw-exception：" + wallFilter.isThrowException());
                System.out.println("WallFilter config：" + JSON.toJSONString(wallFilter.getConfig()));
            }

            if(filter instanceof StatFilter){
                StatFilter statFilter = (StatFilter) filter;
                System.out.println("StatFilter db-type：" + statFilter.getDbType());
                System.out.println("StatFilter log-slow-sql：" + statFilter.isLogSlowSql());
                System.out.println("StatFilter slow-sql-millis：" + statFilter.getSlowSqlMillis());
            }

        }
        System.out.println("DruidDataSource 数据源所有Filter:" + JSON.toJSONString(druidDataSource.getFilterClassNames()));

        connection.close();
    }

    @Test
    public void testSeq(){
        log.info(sequence.getSequence() + "");
    }

    @Test
    public void testBiBox(){
        ServiceRequest request = ServiceRequest.newInstance();
        request.setAppId("service-batch");
        request.setServiceId("CLIENT_BIBOX");
        request.setMethodName("getBiBoxQuotes");
        request.setParamValue("productCoinName", "BTC");
        request.setParamValue("priceCoinName", "USDT");

        log.info(JSON.toJSONString(biBoxService.invoke(request)));
    }

    @Test
    public void testBitZ(){
        ServiceRequest request = ServiceRequest.newInstance();
        request.setAppId("service-batch");
        request.setServiceId("CLIENT_BIT_Z");
        request.setMethodName("getBitZQuotes");
        request.setParamValue("productCoinName", "BTC");
        request.setParamValue("priceCoinName", "USDT");

        log.info(JSON.toJSONString(bitZService.invoke(request)));
    }

    @Test
    public void testCache2(){
        log.info(JSON.toJSONString(cache.get("CrudService:")));
    }

    @Test
    public void testSetCache(){
        log.info("{} add {} value", setName, cache.sAdd(setName, "hello", "222"));
        log.info("{} add {} value", setName, cache.sAdd(setName, "222", "world"));
        log.info("{} has {} value", setName, cache.sCard(setName));
        log.info("{} has {}: {}", setName, "222", cache.sIsMember(setName, "222"));
        log.info("{} delete {} value", setName, cache.sRem(setName, "222"));
        log.info("{} values: {}", setName, JSON.toJSONString(cache.sMembers(setName, String.class)));
        cache.evict("SET", "CURRENT");
    }

}
