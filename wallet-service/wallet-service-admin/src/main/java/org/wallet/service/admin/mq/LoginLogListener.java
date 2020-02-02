package org.wallet.service.admin.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wallet.common.constants.MQConsumerGroup;
import org.wallet.common.constants.MQTopic;
import org.wallet.common.dto.admin.SysLoginLogDTO;
import org.wallet.dap.mq.RocketMQListener;
import org.wallet.dap.mq.annotation.MessageListener;
import org.wallet.service.admin.service.SysLogService;

/**
 * @author zengfucheng
 **/
@Component
@MessageListener(consumerGroup = MQConsumerGroup.ADMIN_LOGIN_LOG, topic = MQTopic.ADMIN_LOGIN_LOG)
public class LoginLogListener implements RocketMQListener<SysLoginLogDTO> {

    @Autowired
    SysLogService logService;

    @Override
    public void onMessage(SysLoginLogDTO message) {
        logService.saveLogin(message);
    }
}
