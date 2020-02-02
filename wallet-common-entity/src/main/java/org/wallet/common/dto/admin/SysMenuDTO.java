package org.wallet.common.dto.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalTreeDTO;

/**
 * 菜单
 *
 * @author zengfucheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuDTO extends BaseNormalTreeDTO<SysMenuDTO> {
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 授权(多个用逗号分隔，如：sys:user:list,sys:user:save)
     */
    private String permissions;
    /**
     * 类型   0：菜单   1：按钮
     */
    private Integer type;
    /**
     * 菜单图标
     */
    private String icon;

}