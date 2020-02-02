package org.wallet.service.application.service.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.field.AppChainField;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.StatisticsService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_STATISTICS)
public class StatisticsDubboService extends BaseDubboService implements IService {

    @Autowired
    StatisticsService statisticsService;

    public ServiceResponse getAssetsStatisticsByChainId(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppChainField.APP_CHAIN_ID);
        }

        return response.setResult(statisticsService.assetsStatistics(id));
    }
}
