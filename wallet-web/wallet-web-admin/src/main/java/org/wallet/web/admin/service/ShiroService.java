package org.wallet.web.admin.service;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.AdminUserCache;
import org.wallet.common.dto.admin.SysUserDTO;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;

import java.util.List;
import java.util.Set;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
public class ShiroService {

    @Autowired
    private Cache cache;

    @Value("${spring.application.name}")
    private String applicationName;

    @Reference(group = DubboServiceGroup.SERVICE_SYS_DEPT)
    private IService deptService;

    @Reference(group = DubboServiceGroup.SERVICE_SYS_MENU)
    private IService menuService;

    @Reference(group = DubboServiceGroup.SERVICE_SYS_USER)
    private IService userService;

    /**
     * 获取用户权限列表
     * @param userId 用户ID
     * @param superAdmin 超级用户
     * @return 权限列表
     */
    @SuppressWarnings("unchecked")
    public Set<String> getUserPermissions(Long userId, Boolean superAdmin){
        Set<String> result = cache.get(AdminUserCache.PERMISSIONS, userId.toString(), Set.class);
        if(null != result){
            return result;
        }
        ServiceRequest request = ServiceRequest.newInstance(applicationName, DubboServiceGroup.SERVICE_SYS_MENU,
                "getUserPermissions", userId, superAdmin);
        ServiceResponse response = menuService.invoke(request);
        if(response.success()){
            if(null != response.getResult()) {
                cache.put(AdminUserCache.PERMISSIONS, userId.toString(), response.getResult(), AdminUserCache.EXPIRE);
            }
            return response.getResult();
        }else{
            log.warn("获取用户[{}]权限失败[{}][{}]", request.getUserId(), response.getRespCode(), response.getRespMsg());
            return null;
        }
    }

    /**
     * 根据用户ID，查询用户
     * @param userId 用户ID
     * @return 用户
     */
    public SysUserDTO getUser(Long userId){
        SysUserDTO result = cache.get(AdminUserCache.USER_BY_ID, userId.toString(), SysUserDTO.class);
        if(null != result){
            return result;
        }
        ServiceRequest request = ServiceRequest.newInstance(applicationName, DubboServiceGroup.SERVICE_SYS_USER,
                "getSysUserById", userId, userId);
        ServiceResponse response = userService.invoke(request);
        if(response.success()){
            if(null != response.getResult()) {
                cache.put(AdminUserCache.USER_BY_ID, userId.toString(), response.getResult(), AdminUserCache.EXPIRE);
            }
            return response.getResult();
        }else{
            log.warn("获取用户[{}]失败[{}][{}]", userId, response.getRespCode(), response.getRespMsg());
            return null;
        }
    }

    /**
     * 根据用户名，查询用户
     * @param username 用户名
     * @return 用户
     */
    public SysUserEntity getUser(String username){
        SysUserEntity result = cache.get(AdminUserCache.USER_BY_USERNAME, username, SysUserEntity.class);
        if(null != result){
            return result;
        }
        ServiceRequest request = ServiceRequest.newInstance(applicationName, DubboServiceGroup.SERVICE_SYS_USER,
                "findSysUserByUsername", null, username);
        ServiceResponse response = userService.invoke(request);
        if(response.success()){
            if(null != response.getResult()){
                cache.put(AdminUserCache.USER_BY_USERNAME, username, response.getResult(), AdminUserCache.EXPIRE);
            }
            return response.getResult();
        }else{
            log.warn("获取用户[{}]失败[{}][{}]", username, response.getRespCode(), response.getRespMsg());
            return null;
        }
    }

    /**
     * 获取用户对应的部门数据权限
     * @param userId  用户ID
     * @return 返回部门ID列表
     */
    @SuppressWarnings("unchecked")
    public List<Long> findDataScopeList(Long userId){
        List<Long> result = cache.get(AdminUserCache.DATA_SCOPE, userId.toString(), List.class);
        if(null != result){
            return result;
        }
        ServiceRequest request = ServiceRequest.newInstance(applicationName, DubboServiceGroup.SERVICE_SYS_DEPT,
                "findDataScopeList", userId);
        ServiceResponse response = deptService.invoke(request);
        if(response.success()){
            if(null != response.getResult()){
                cache.put(AdminUserCache.DATA_SCOPE, userId.toString(), response.getResult(), AdminUserCache.EXPIRE);
            }
            return response.getResult();
        }else{
            log.warn("获取用户[{}]部门数据权限失败[{}][{}]", userId, response.getRespCode(), response.getRespMsg());
            return null;
        }
    }
}
