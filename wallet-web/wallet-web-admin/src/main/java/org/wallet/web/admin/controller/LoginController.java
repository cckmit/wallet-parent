package org.wallet.web.admin.controller;

import cn.hutool.core.codec.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.common.constants.WebConstants;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.admin.AdminToken;
import org.wallet.common.dto.admin.LoginDTO;
import org.wallet.common.dto.admin.SysUserDTO;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.common.enums.ResultCode;
import org.wallet.common.enums.admin.LoginOperationEnum;
import org.wallet.common.enums.admin.LoginStatusEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.crypto.CryptoException;
import org.wallet.dap.common.crypto.digest.Md5;
import org.wallet.dap.common.crypto.symmetric.AES;
import org.wallet.web.admin.config.AdminProperties;
import org.wallet.web.admin.service.LogService;
import org.wallet.web.admin.service.ShiroService;
import org.wallet.web.admin.utils.PasswordUtils;
import org.wallet.web.common.mvc.controller.BaseController;
import org.wallet.web.common.utils.TokenUtil;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * 登录
 * 
 * @author zengfucheng
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    private Cache cache;

    @Autowired
    private AdminProperties adminProperties;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private LogService logService;

    private Md5 md5 = new Md5();

    /**
     * 登录
     * 登录密码需加密
     * 加密方式：AES/ECB/Pkcs7/Base64
     * 加密Key：MD5(username)最后16位
     * @param login 登录数据
     * @return 结果
     */
	@PostMapping("login")
	public SimpleResult<?> login(@Valid @RequestBody LoginDTO login) {
	    Long userId = null;
        LoginStatusEnum status = LoginStatusEnum.FAIL;
	    try{
            String username = login.getUsername();
            String password = login.getPassword();
            SysUserEntity userEntity = shiroService.getUser(username);
            if(userEntity == null){
                status = LoginStatusEnum.USERNAME_INVALID;
                return Results.byCode(ResultCode.AdminUsernameInvalid);
            }

            userId = userEntity.getId();

            String usernameMd5 = md5.encrypt(username).toLowerCase();

            String key = usernameMd5.substring(usernameMd5.length() - 16);

            if(StringUtils.isEmpty(key)){
                return Results.fail("获取密码Key失败");
            }

            AES aes = new AES(key.getBytes());

            try{
                password = new String(aes.decrypt(Base64.decode(password.getBytes())), StandardCharsets.UTF_8);
            } catch (CryptoException ignored){}


            if(!PasswordUtils.matches(password, userEntity.getPassword())){
                status = LoginStatusEnum.PASSWORD_INVALID;
                return Results.byCode(ResultCode.AdminPasswordInvalid);
            }

            if(Integer.valueOf(0).equals(userEntity.getStatus())){
                status = LoginStatusEnum.LOCKED_ACCOUNT;
                return Results.byCode(ResultCode.AdminLockedAccount);
            }

            status = LoginStatusEnum.SUCCESS;

            Duration timeout = adminProperties.getTokenTimeout();

            AdminToken adminToken = new AdminToken();
            adminToken.setUsername(userEntity.getUsername());
            adminToken.setToken(TokenUtil.createToken(adminToken, timeout));
            long expire = (System.currentTimeMillis() + timeout.toMillis()) / 1000;
            adminToken.setExpire(Long.toString(expire));

            cache.put(WebConstants.CACHE_ADMIN_TOKEN, adminToken.getUsername(), adminToken, timeout.getSeconds());

            return Results.success(adminToken);
        } finally {
            logService.loginLog(request, userId, login.getUsername(), LoginOperationEnum.LOGIN, status);
        }
	}

	@PostMapping("logout")
	public SimpleResult<?> logout() {
        Subject subject = SecurityUtils.getSubject();
        SysUserDTO userDTO = (SysUserDTO) subject.getPrincipal();
        logService.loginLog(request, userDTO.getId(), userDTO.getUsername(), LoginOperationEnum.LOGOUT, LoginStatusEnum.SUCCESS);
        cache.evict(WebConstants.CACHE_ADMIN_TOKEN, userDTO.getUsername());
        subject.logout();
		return Results.success();
	}

}