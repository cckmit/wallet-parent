package org.wallet.service.admin.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wallet.common.constants.MQConsumerGroup;
import org.wallet.common.constants.MQTopic;
import org.wallet.common.dto.admin.SysOperationLogDTO;
import org.wallet.dap.mq.RocketMQListener;
import org.wallet.dap.mq.annotation.MessageListener;
import org.wallet.service.admin.service.SysLogService;

/**
 * @author zengfucheng
 **/
@Component
@MessageListener(consumerGroup = MQConsumerGroup.ADMIN_OPERATION_LOG, topic = MQTopic.ADMIN_OPERATION_LOG)
public class OperationLogListener implements RocketMQListener<SysOperationLogDTO> {

    @Autowired
    SysLogService logService;

    @Override
    public void onMessage(SysOperationLogDTO message) {
        logService.saveOperation(message);
    }
}
