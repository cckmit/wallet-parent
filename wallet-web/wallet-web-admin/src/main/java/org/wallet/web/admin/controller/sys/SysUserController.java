package org.wallet.web.admin.controller.sys;

import cn.hutool.core.codec.Base64;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.admin.SysUserDTO;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.common.enums.ResultCode;
import org.wallet.common.enums.admin.LoginStatusEnum;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.crypto.CryptoException;
import org.wallet.dap.common.crypto.symmetric.AES;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.web.admin.annotation.OperationLog;
import org.wallet.web.admin.service.ShiroService;
import org.wallet.web.admin.shiro.P;
import org.wallet.web.admin.utils.PasswordUtils;
import org.wallet.web.admin.utils.UserUtil;
import org.wallet.web.common.mvc.controller.BaseController;
import org.wallet.web.common.utils.TokenUtil;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

/**
 * @author zengfucheng
 */
@RestController
@RequestMapping("sys/user")
public class SysUserController extends BaseController {

    @Autowired
    private ShiroService shiroService;

    @Reference(group = DubboServiceGroup.SERVICE_SYS_USER)
    private IService sysUserService;

    @Reference(group = DubboServiceGroup.SERVICE_SYS_MENU)
    private IService sysMenuService;

    public SysUserController() {
        setServiceGroup(DubboServiceGroup.SERVICE_SYS_USER);
    }

    @GetMapping("current")
    public SimpleResult currentSysUser() {
        return Results.success(UserUtil.getUser());
    }

    @GetMapping("menus")
    public SimpleResult menus(){
        ServiceResponse response = sysMenuService.invoke(createRequest("getUserMenus", UserUtil.getUserId(), UserUtil.getUser().getSuperAdmin()));

        return Results.by(response);
    }

    @GetMapping("{id:\\d{1,19}}")
    @RequiresPermissions(P.SYS_USER + P.INFO)
    public SimpleResult findById(@PathVariable Long id){
        ServiceResponse response = sysUserService.invoke(createRequest("getSysUserById", UserUtil.getUserId(), id));

        return Results.by(response);
    }

    @GetMapping("all")
    @RequiresPermissions(P.SYS_USER + P.INFO)
    public SimpleResult findAll(){
        ServiceResponse response = sysUserService.invoke(createRequest("findAllSysUser", UserUtil.getUserId()));

        return Results.by(response);
    }

    @GetMapping
    @RequiresPermissions(P.SYS_USER + P.INFO)
    public SimpleResult find(SysUserDTO dto, PageDTO page, SortDTO sort){
        Search search = new Search(dto, page, sort);

        ServiceResponse response = sysUserService.invoke(createRequest("findSysUser", UserUtil.getUserId(), search));

        return Results.by(response);
    }

    /**
     * 添加用户数据
     * 密码需加密
     * 加密方式：AES/ECB/Pkcs7/Base64
     * 加密Key：MD5(username)最后16位
     * @param dto 用户数据
     * @return 结果
     */
    @PostMapping
    @OperationLog("添加用户")
    @RequiresPermissions(P.SYS_USER + P.SAVE)
    public ResponseEntity<?> post(@RequestBody @Valid SysUserDTO dto){
        String token = TokenUtil.getToken(request);

        String key = token.substring(token.length() - 16);

        AES aes = new AES(key.getBytes());

        // 解密密码
        String desPwd = null;
        try {
            desPwd = new String(aes.decrypt(Base64.decode(dto.getPassword().getBytes())), StandardCharsets.UTF_8);
        } catch (CryptoException ignored) {}

        if(StringUtils.isEmpty(desPwd)){
            return ResponseEntity.ok(Results.fail("解密密码失败"));
        }

        // 加密密码以便保存
        dto.setPassword(PasswordUtils.encode(desPwd));

        ServiceResponse response = sysUserService.invoke(createRequest("addSysUser", UserUtil.getUserId(), dto));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    /**
     * 修改用户数据
     * 修改密码时原密码和新密码需加密
     * 加密方式：AES/ECB/Pkcs7/Base64
     * 加密Key：MD5(username)最后16位
     * @param id 用户ID
     * @param dto 用户数据
     * @return 结果
     */
    @OperationLog("修改用户")
    @RequiresPermissions(P.SYS_USER + P.UPDATE)
    @RequestMapping(value = "{id:\\d{1,19}}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody SysUserDTO dto){
        dto.setId(id);

        SysUserDTO userDTO = shiroService.getUser(id);

        if(null == userDTO){
            return ResponseEntity.ok(Results.byCode(ResultCode.AdminUsernameInvalid));
        }

        SysUserEntity userEntity = shiroService.getUser(userDTO.getUsername());

        String password = dto.getPassword();
        String oldPassword = dto.getOldPassword();

        if(!StringUtils.isEmpty(password)
                && !StringUtils.isEmpty(oldPassword)){
            String token = TokenUtil.getToken(request);

            String key = token.substring(token.length() - 16);

            AES aes = new AES(key.getBytes());

            String desPwd = null;
            String desOldPwd = null;
            try{
                desPwd = new String(aes.decrypt(Base64.decode(password.getBytes())), StandardCharsets.UTF_8);
                desOldPwd = new String(aes.decrypt(Base64.decode(oldPassword.getBytes())), StandardCharsets.UTF_8);
            } catch (CryptoException ignored){}

            if(StringUtils.isEmpty(desPwd) || StringUtils.isEmpty(desOldPwd)){
                return ResponseEntity.ok(Results.fail("解密密码失败"));
            }

            if(!PasswordUtils.matches(desOldPwd, userEntity.getPassword())){
                return ResponseEntity.ok(Results.byCode(ResultCode.AdminPasswordInvalid));
            }

            // 加密密码
            dto.setPassword(PasswordUtils.encode(desPwd));
        }

        ServiceResponse response = sysUserService.invoke(createRequest("updateSysUser", UserUtil.getUserId(), dto));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.CREATED).body(Results.success(response.getResult()));
        }else{
            handleFailResponse(response);
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }

    @OperationLog("删除用户")
    @RequiresPermissions(P.SYS_USER + P.DELETE)
    @DeleteMapping("{id:\\d{1,19}}")
    public ResponseEntity<SimpleResult> delete(@PathVariable Long id){
        ServiceResponse response = sysUserService.invoke(createRequest("deleteSysUser", UserUtil.getUserId(), id));

        if(response.success()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.ok(Results.fail(response.getRespMsg()));
        }
    }
}