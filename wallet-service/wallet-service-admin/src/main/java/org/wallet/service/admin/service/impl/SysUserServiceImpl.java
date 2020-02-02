package org.wallet.service.admin.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.AdminUserCache;
import org.wallet.common.constants.field.SysUserField;
import org.wallet.common.dto.admin.SysUserDTO;
import org.wallet.common.entity.admin.SysRoleUserEntity;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.common.enums.admin.UserStatusEnum;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.service.admin.dao.SysRoleUserJpaDao;
import org.wallet.service.admin.dao.SysUserJpaDao;
import org.wallet.service.admin.service.SysUserService;
import org.wallet.service.common.bind.specification.MySpecification;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.Optional;

/**
 * @author zengfucheng
 **/
@Service
public class SysUserServiceImpl extends AbstractCrudService<SysUserJpaDao, SysUserEntity> implements SysUserService {

    @Autowired
    private SysRoleUserJpaDao sysRoleUserJpaDao;

    @Override
    public SysUserDTO findDTOById(Long id) {
        SysUserEntity entity = findOne(id);

        if(null == entity){
            return null;
        }

        SysUserDTO dto = new SysUserDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setPassword(null);

        Optional<SysRoleUserEntity> op = sysRoleUserJpaDao.findOne(new MySpecification<SysRoleUserEntity>(Searchs.of(SearchFilters.eq(SysUserField.USER_ID, id))).toPredicate());

        op.ifPresent(sysRoleUserEntity -> dto.setRoleId(sysRoleUserEntity.getRoleId()));

        return dto;
    }

    @Override
    public SysUserEntity save(SysUserDTO dto, Long userId) {
        SysUserEntity entity = new SysUserEntity();

        BeanUtils.copyProperties(dto, entity);

        boolean update = null != dto.getId();

        if(null == entity.getDeptId()){
            entity.setDeptId(0L);
        }

        if(update){
            entity.setCoverageUpdate(true);
            entity.setUsername(null);
        }else{
            if(null == entity.getSuperAdmin()){
                entity.setSuperAdmin(Boolean.FALSE);
            }
            if(null == entity.getStatus()){
                entity.setStatus(UserStatusEnum.ENABLED.value());
            }
            if(null == entity.getGender()){
                entity.setGender(2);
            }
            if(null == entity.getDeptId()){
                entity.setDeptId(0L);
            }
            entity.setCreator(userId);
        }
        entity.setUpdater(userId);

        SysUserEntity result = save(entity);

        if(null == result){
            return result;
        }

        if(update){
            cache.evict(AdminUserCache.USER_BY_ID, dto.getId().toString());
            cache.evict(AdminUserCache.USER_BY_USERNAME, result.getUsername());
        }

        if(null != dto.getRoleId()){
            if(update){
                sysRoleUserJpaDao.deleteByUserId(dto.getId());
            }

            SysRoleUserEntity roleUserEntity = new SysRoleUserEntity();

            roleUserEntity.setId(sequence.getSequence());
            roleUserEntity.setCreator(userId);
            roleUserEntity.setUserId(result.getId());
            roleUserEntity.setRoleId(dto.getRoleId());

            sysRoleUserJpaDao.save(roleUserEntity);
        }

        return result;
    }
}
