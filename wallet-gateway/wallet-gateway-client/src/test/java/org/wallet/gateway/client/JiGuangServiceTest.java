package org.wallet.gateway.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.dto.push.PushMessage;
import org.wallet.common.dto.push.PushTarget;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.gateway.client.service.dubbo.JiGuangService;

/**
 * @author zengfucheng
 **/
public class JiGuangServiceTest extends SpringBootJUnitTest {
    @Autowired
    JiGuangService jiGuangService;

    @Test
    public void push(){
        ServiceRequest request = getRequest();

        request.setMethodName("push");

        request.setParamValue("target", PushTarget.all());
        request.setParamValue("message", PushMessage.create("这是一个推送"));

        ServiceResponse response = jiGuangService.invoke(request);

        log.info(JSON.toJSONString(response));
    }
}
