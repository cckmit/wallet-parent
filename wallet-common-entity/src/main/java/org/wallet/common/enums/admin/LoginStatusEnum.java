package org.wallet.common.enums.admin;

/**
 * 登录状态枚举
 *
 * @author zengfucheng
 */
public enum LoginStatusEnum {
    /** 失败 */
    FAIL(-3),
    /** 未知账户 */
    USERNAME_INVALID(-2),
    /** 密码错误 */
    PASSWORD_INVALID(-1),
    /** 成功 */
    SUCCESS(0),
    /** 账号已锁定 */
    LOCKED_ACCOUNT(1);

    private int value;

    LoginStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
