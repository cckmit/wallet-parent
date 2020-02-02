package org.wallet.common.enums.admin;

/**
 * 操作状态枚举
 *
 * @author zengfucheng
 */
public enum OperationStatusEnum {
    /**
     * 异常
     */
    EXCEPTION(-2),
    /**
     * 业务失败
     */
    FAIL(-1),
    /**
     * 成功
     */
    SUCCESS(0);

    private int value;

    OperationStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}