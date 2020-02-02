package org.wallet.common.enums.admin;

/**
 * 超级管理员枚举
 *
 * @author zengfucheng
 */
public enum SuperAdminEnum {
    /** 超级管理员 */
    YES(1),
    /** 普通管理员 */
    NO(0);

    private int value;

    SuperAdminEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}