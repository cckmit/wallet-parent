package org.wallet.common.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录表单
 *
 * @author zengfucheng
 */
@Data
public class LoginDTO implements Serializable {
    @NotBlank(message="用户名不能为空")
    private String username;

    @NotBlank(message="密码不能为空")
    private String password;
}