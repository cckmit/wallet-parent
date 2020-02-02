package org.wallet.common.enums.admin;

/**
 * 菜单类型枚举
 *
 * @author zengfucheng
 */
public enum MenuTypeEnum {
    /** 菜单 */
    MENU(0),
    /** 功能 */
    FUNCTION(1);

    private int value;

    MenuTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
