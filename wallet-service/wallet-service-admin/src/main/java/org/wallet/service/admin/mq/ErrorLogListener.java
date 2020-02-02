package org.wallet.service.admin.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wallet.common.constants.MQConsumerGroup;
import org.wallet.common.constants.MQTopic;
import org.wallet.common.dto.admin.SysErrorLogDTO;
import org.wallet.dap.mq.RocketMQListener;
import org.wallet.dap.mq.annotation.MessageListener;
import org.wallet.service.admin.service.SysLogService;

/**
 * @author zengfucheng
 **/
@Component
@MessageListener(consumerGroup = MQConsumerGroup.ADMIN_ERROR_LOG, topic = MQTopic.ADMIN_ERROR_LOG)
public class ErrorLogListener implements RocketMQListener<SysErrorLogDTO> {

    @Autowired
    SysLogService logService;

    @Override
    public void onMessage(SysErrorLogDTO message) {
        logService.saveError(message);
    }
}
