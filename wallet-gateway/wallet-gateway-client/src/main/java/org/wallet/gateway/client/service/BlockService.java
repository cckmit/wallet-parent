package org.wallet.gateway.client.service;

import org.wallet.common.dto.block.BlockResult;
import org.wallet.common.dto.block.req.CreateAccountReqDTO;

/**
 * @author zengfucheng
 **/
public interface BlockService {
    /**
     * 获取币种名称
     *
     * @return 币种名称
     */
    String getCoinName();

    /**
     * 创建账号
     *
     * @param dto 创建账号请求数据
     * @return 响应结果
     */
    BlockResult createAccount(CreateAccountReqDTO dto);
}
