package org.wallet.service.admin.service.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.field.SysLogField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.admin.SysLoginLogDTO;
import org.wallet.common.dto.admin.SysOperationLogDTO;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.admin.service.SysLogService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_SYS_LOG)
public class SysLogDubboService extends BaseDubboService implements IService {

    @Autowired
    SysLogService sysLogService;

    public ServiceResponse findSysOperationLogPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(SysLogField.PAGE); }

        PageDTO<SysOperationLogDTO> page = sysLogService.findOperationPage(search);

        return response.setResult(page);
    }

    public ServiceResponse findSysLoginLogPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(SysLogField.PAGE); }

        PageDTO<SysLoginLogDTO> page = sysLogService.findLoginPage(search);

        return response.setResult(page);
    }

}
