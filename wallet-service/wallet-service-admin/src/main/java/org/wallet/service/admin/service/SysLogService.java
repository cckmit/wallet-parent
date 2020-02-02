package org.wallet.service.admin.service;

import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.admin.SysErrorLogDTO;
import org.wallet.common.dto.admin.SysLoginLogDTO;
import org.wallet.common.dto.admin.SysOperationLogDTO;
import org.wallet.common.entity.admin.SysErrorLogEntity;
import org.wallet.common.entity.admin.SysLoginLogEntity;
import org.wallet.common.entity.admin.SysOperationLogEntity;
import org.wallet.dap.common.bind.search.Search;

/**
 * @author zengfucheng
 **/
public interface SysLogService {

    /**
     * 保存错误日志
     * @param errorEntity 错误日志
     * @return 错误日志
     */
    SysErrorLogEntity saveError(SysErrorLogDTO errorEntity);

    /**
     * 保存登录日志
     * @param loginEntity 登录日志
     * @return 登录日志
     */
    SysLoginLogEntity saveLogin(SysLoginLogDTO loginEntity);

    /**
     * 保存操作日志
     * @param operationLog 操作日志
     * @return 操作日志
     */
    SysOperationLogEntity saveOperation(SysOperationLogDTO operationLog);

    /**
     * 分页查询操作日志
     * @param search 查询条件
     * @return 分页数据
     */
    PageDTO<SysOperationLogDTO> findOperationPage(Search search);

    /**
     * 分页查询登录日志
     * @param search 查询条件
     * @return 分页数据
     */
    PageDTO<SysLoginLogDTO> findLoginPage(Search search);
}
