package org.wallet.web.admin.controller.sys;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.admin.SysLoginLogDTO;
import org.wallet.common.dto.admin.SysOperationLogDTO;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.web.admin.shiro.P;
import org.wallet.web.admin.utils.UserUtil;
import org.wallet.web.common.mvc.controller.BaseController;

/**
 * @author zengfucheng
 */
@RestController
@RequestMapping("sys/log")
public class SysLogController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_SYS_LOG)
    private IService sysLogService;

    public SysLogController() {
        setServiceGroup(DubboServiceGroup.SERVICE_SYS_LOG);
    }

    @GetMapping("operation")
    @RequiresPermissions(P.SYS_LOG + P.INFO)
    public SimpleResult operationLog(SysOperationLogDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = sysLogService.invoke(createRequest("findSysOperationLogPage", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("login")
    @RequiresPermissions(P.SYS_LOG + P.INFO)
    public SimpleResult loginLog(SysLoginLogDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = sysLogService.invoke(createRequest("findSysLoginLogPage", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    @GetMapping("login/self")
    @RequiresPermissions(P.SYS_LOG + P.INFO)
    public SimpleResult selfLoginLog(SysLoginLogDTO dto, PageDTO page, SortDTO sort){
        Long userId = UserUtil.getUserId();
        if(null == dto.getCreator()){
            dto.setCreator(userId);
        }
        Search search = new Search(dto, page, sort);

        ServiceResponse response = sysLogService.invoke(createRequest("findSysLoginLogPage", userId, search));

        return Results.by(response);
    }

}