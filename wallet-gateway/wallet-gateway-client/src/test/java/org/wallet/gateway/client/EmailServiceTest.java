package org.wallet.gateway.client;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.gateway.client.service.dubbo.DFuseService;
import org.wallet.gateway.client.service.dubbo.EmailService;

/**
 * @author zengfucheng
 **/
public class EmailServiceTest extends SpringBootJUnitTest{

    @Autowired
    EmailService emailService;

    @Test
    public void sendMail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("servicepandoras@gmail.com");
        message.setTo("snzke@live.cn");
        message.setSubject("Activation Code");
        message.setText("【Pandora Wallet】Please note that your account registration invitation code is：49284893");
        emailService.sendMail(ServiceRequest.newInstance("wallet-gateway-client", DubboServiceGroup.CLIENT_EMAIL,
                "sendMail", null, message), ServiceResponse.newInstance());
    }
}
