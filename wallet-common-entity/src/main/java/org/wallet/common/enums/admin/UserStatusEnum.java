package org.wallet.common.enums.admin;

/**
 * 用户状态
 *
 * @author zengfucheng
 */
public enum UserStatusEnum {
    /** 禁用 */
    DISABLE(0),
    /** 启用 */
    ENABLED(1);

    private int value;

    UserStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
