package org.wallet.web.admin.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.wallet.common.dto.admin.SysUserDTO;

/**
 * @author zengfucheng
 **/
public class UserUtil {

    public static Subject getSubject() {
        try {
            return SecurityUtils.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户信息
     */
    public static SysUserDTO getUser() {
        Subject subject = getSubject();
        if (subject == null) {
            return new SysUserDTO();
        }

        SysUserDTO user = (SysUserDTO) subject.getPrincipal();
        if (user == null) {
            return new SysUserDTO();
        }

        return user;
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return getUser().getId();
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        return getUser().getDeptId();
    }
}
