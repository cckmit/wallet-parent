package org.wallet.common.enums.wallet;

/**
 * @author zengfucheng
 **/
public enum WalletCoinOrderStatusEnum {
    /** 初始化状态 */
    INIT,
    /** 支付金额不足 */
    INSUFFICIENT_QUANTITY,
    /** 获取邀请码失败 */
    GET_CODE_FAIL,
    /** 订单完成 */
    FINISH,
    /** 发送邀请码失败 */
    SEND_FAIL
}
