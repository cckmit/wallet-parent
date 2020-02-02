package org.wallet.gateway.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.gateway.client.service.dubbo.OssService;

/**
 * @author zengfucheng
 **/
public class OssServiceTest extends SpringBootJUnitTest {
    @Autowired
    OssService ossService;

    @Test
    public void headPath(){
        ServiceRequest request = getRequest();

        request.setMethodName("headPath");
        ServiceResponse response = ossService.invoke(request);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void getOssStsToken(){
        ServiceRequest request = getRequest();

        request.setMethodName("getOssStsToken");
        ServiceResponse response = ossService.invoke(request);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void getOssStsTokenService(){
        ServiceRequest request = getRequest();

        request.setMethodName("getOssStsTokenService");
        ServiceResponse response = ossService.invoke(request);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void getOssSignature(){
        ServiceRequest request = getRequest();

        request.setMethodName("getOssSignature");
        ServiceResponse response = ossService.invoke(request);
        log.info(JSON.toJSONString(response));
    }
}
