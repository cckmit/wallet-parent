package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.wallet.dap.common.dubbo.*;

import javax.annotation.PostConstruct;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service(group = DubboServiceGroup.CLIENT_EMAIL, timeout = 30000)
@org.springframework.stereotype.Service
public class EmailService extends BaseDubboService implements IService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties properties;

    @PostConstruct
    public void viewMailProperties(){
        log.info("MailProperties : " + JSON.toJSONString(properties));
    }

    public ServiceResponse sendMail(ServiceRequest request, ServiceResponse response) {
        SimpleMailMessage message = request.getParam();
        if(null == message){ Responses.missingParam("message"); }

        try{
            mailSender.send(message);
        } catch (Exception e){
            log.error("发送邮件[{}]{}[{}]失败：{}", message.getFrom(), message.getTo(), message.getSubject(), e.getMessage());
        }

        return Responses.success();
    }
}
