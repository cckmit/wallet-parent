package org.wallet.service.application.service;

import org.wallet.common.dto.wallet.WalletCoinDTO;
import org.wallet.common.dto.wallet.req.FindCoinInfoReqDTO;
import org.wallet.common.entity.wallet.WalletCoinEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletCoinService extends CrudService<WalletCoinEntity> {
    /**
     * 保存币种信息
     * @param dto 币种信息
     * @param userId 用户ID
     * @return 币种信息
     */
    WalletCoinDTO save(WalletCoinDTO dto, Long userId);

    /**
     * 根据ID查询币种信息
     * @param id
     * @return
     */
    WalletCoinDTO findDTOById(Long id);

    /**
     * 根据参数查询币种信息
     * @param findCoinInfoReqDTO 参数
     * @return 币种信息
     */
    List<WalletCoinDTO> findCoinInfo(FindCoinInfoReqDTO findCoinInfoReqDTO);

    /**
     * 根据参数查询币种基本信息
     * @param findCoinInfoReqDTO 参数
     * @return 币种基本信息
     */
    List<WalletCoinDTO> findCoinBaseInfo(FindCoinInfoReqDTO findCoinInfoReqDTO);
}
