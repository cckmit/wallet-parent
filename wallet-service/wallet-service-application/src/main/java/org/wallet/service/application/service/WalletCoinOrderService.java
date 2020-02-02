package org.wallet.service.application.service;

import org.wallet.common.dto.chart.ChartDataDTO;
import org.wallet.common.dto.chart.PercentDataDTO;
import org.wallet.common.dto.wallet.req.TransferStatisticsReqDTO;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.common.enums.wallet.PaymentTypeEnum;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletCoinOrderService extends CrudService<WalletCoinOrderEntity> {
    /**
     * 创建订单
     * @param coinName 币种名称
     * @param type 支付类型
     * @param email email
     * @return 订单
     */
    ServiceResponse createOrder(String coinName, PaymentTypeEnum type, String email);

    /**
     * 购买币种账户订单统计
     * @param reqDTO 查询条件
     * @return 图表数据
     */
    List<ChartDataDTO> accountBuyFrequency(TransferStatisticsReqDTO reqDTO);

    /**
     * 购买币种账户订单方式占比
     * @param reqDTO 查询条件
     * @return 图表数据
     */
    List<PercentDataDTO> accountBuyPercent(TransferStatisticsReqDTO reqDTO);
}
