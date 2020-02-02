package org.wallet.gateway.client;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.wallet.common.entity.platform.bibox.BiBoxResult;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.cache.lock.DapLock;
import org.wallet.dap.common.utils.ThreadPool;
import org.wallet.gateway.client.dao.BiBoxDao;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 应用启动监听器
 * @author zengfucheng
 **/
@Slf4j
//@Component
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    Cache cache;

    @Autowired
    BiBoxDao biBoxDao;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        cache.clear();
        BiBoxResult<String> result = new BiBoxResult<>();
        result.setCmd("thisCmd");
        result.setVer("2.0.4");
        result.setResult("水电费");
        cache.put("bibox", "result", result, 30L);

        BiBoxResult cacheResult = cache.get("bibox", "result", BiBoxResult.class);

        System.out.println(JSON.toJSONString(cacheResult));

        System.out.println(JSON.toJSONString(biBoxDao.getResultByVer("2.0.5")));
        System.out.println(JSON.toJSONString(biBoxDao.getResultByVer("2.0.5")));
        System.out.println(JSON.toJSONString(biBoxDao.getResultByVer("2.0.6")));

        AtomicInteger atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < 10; i++) {
            ThreadPool.getInstance().exe(() -> {
                DapLock lock = new DapLock("test", 60);
                boolean locked = false;
                try{
                    locked = lock.tryLock(5L, TimeUnit.SECONDS);
                    if(locked){
                        log.info("开始执行业务");
                        Thread.sleep(1000);
                        atomicInteger.addAndGet(1);
                        log.info("业务执行完成：{}", atomicInteger.get());
                    } else {
                        log.info("获取锁超时：{}", atomicInteger.get());
                    }
                } catch (InterruptedException e) {
                    log.error("执行业务失败：{}", e.getMessage(), e);
                } finally {
                    if(locked){
                        lock.unlock();
                    }
                }
            });
        }
    }
}

