package org.wallet.common.dto.admin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.dto.SearchOperator;
import org.wallet.common.dto.SearchProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDTO extends BaseNormalDTO {

    /**
     * 用户名
     */
    @NotBlank
    @SearchProperty(SearchOperator.allLike)
    private String username;
    /**
     * 密码
     */
    @NotBlank
    @JSONField(serialize = false)
    private String password;
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 姓名
     */
    @NotBlank
    @SearchProperty(SearchOperator.allLike)
    private String realName;
    /**
     * 头像
     */
    private String headUrl;
    /**
     * 性别   0：男   1：女    2：保密
     */
    private Integer gender;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    @SearchProperty(SearchOperator.allLike)
    private String mobile;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 超级管理员   0：否   1：是
     */
    private Boolean superAdmin;
    /**
     * 状态  0：停用   1：正常
     */
    private Integer status;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门数据权限
     */
    private List<Long> deptIdList;
    /**
     * 角色ID
     */
    @NotNull
    private Long roleId;
}
