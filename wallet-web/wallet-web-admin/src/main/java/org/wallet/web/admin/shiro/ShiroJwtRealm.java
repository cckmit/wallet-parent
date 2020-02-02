package org.wallet.web.admin.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.WebConstants;
import org.wallet.common.dto.admin.AdminToken;
import org.wallet.common.dto.admin.SysUserDTO;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.cache.Cache;
import org.wallet.web.admin.service.ShiroService;
import org.wallet.web.common.mvc.WebException;
import org.wallet.web.common.mvc.token.TokenException;
import org.wallet.web.common.utils.TokenUtil;

import java.util.List;
import java.util.Set;

/**
 * JWT认证
 *
 * @author zengfucheng
 */
@Slf4j
@Component
public class ShiroJwtRealm extends AuthorizingRealm {
    @Autowired
    private Cache cache;

	@Autowired
	private ShiroService shiroService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof ShiroJwtToken;
	}

	/**
     * 授权(验证权限时调用)
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserDTO user = (SysUserDTO) principals.getPrimaryPrincipal();

        Boolean superAdmin = user.getSuperAdmin();

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		if(superAdmin){
            info.addStringPermission(P.SYS);
            info.addStringPermission(P.APP);
            info.addStringPermission(P.WALLET);
            info.addStringPermission(P.TABLE);
        }else{
            //用户权限列表
            Set<String> permsSet = shiroService.getUserPermissions(user.getId(), user.getSuperAdmin());

            info.setStringPermissions(permsSet);
        }

		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authToken) throws AuthenticationException {
        String token = (String) authToken.getPrincipal();

        if (StringUtils.isEmpty(token)) {
            throw new TokenException(ResultCode.MissingToken);
        }

        AdminToken adminToken;

        try {
            adminToken = TokenUtil.verifyToken(token, AdminToken.class);
        } catch (TokenExpiredException e){
            throw new TokenException(ResultCode.TokenExpired);
        }catch (JWTVerificationException e){
            log.warn("无效Token[{}]:{}", token, e.getMessage());
            throw new TokenException(ResultCode.TokenInvalid);
        }

        AdminToken cacheToken = cache.get(WebConstants.CACHE_ADMIN_TOKEN, adminToken.getUsername(), AdminToken.class);

        if(null == cacheToken){
            throw new TokenException(ResultCode.TokenExpired);
        }

        if(!token.equals(cacheToken.getToken())){
            throw new TokenException(ResultCode.TokenInvalid);
        }

        //查询用户信息
        SysUserEntity userEntity = shiroService.getUser(adminToken.getUsername());
        if(userEntity == null){
            log.warn("无此用户[{}]", adminToken.getUsername());
            throw new TokenException(ResultCode.TokenInvalid);
        }

        //账号锁定
        if(Integer.valueOf(0).equals(userEntity.getStatus())){
            throw new WebException(ResultCode.AdminLockedAccount);
        }

        SysUserDTO sysUser = new SysUserDTO();

        BeanUtil.copyProperties(userEntity, sysUser);

        //获取用户对应的部门数据权限
        List<Long> deptIdList = shiroService.findDataScopeList(sysUser.getId());
        sysUser.setDeptIdList(deptIdList);

        return new SimpleAuthenticationInfo(sysUser, token, getName());
	}

}
