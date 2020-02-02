package org.wallet.service.admin.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.admin.SysRoleDTO;
import org.wallet.common.entity.admin.SysRoleDataScopeEntity;
import org.wallet.common.entity.admin.SysRoleEntity;
import org.wallet.common.entity.admin.SysRoleMenuEntity;
import org.wallet.service.admin.dao.SysRoleDataScopeJpaDao;
import org.wallet.service.admin.dao.SysRoleJpaDao;
import org.wallet.service.admin.dao.SysRoleMenuJpaDao;
import org.wallet.service.admin.dao.SysRoleUserJpaDao;
import org.wallet.service.admin.service.SysRoleService;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class SysRoleServiceImpl extends AbstractCrudService<SysRoleJpaDao, SysRoleEntity> implements SysRoleService {

    @Autowired
    private SysRoleMenuJpaDao sysRoleMenuJpaDao;

    @Autowired
    private SysRoleDataScopeJpaDao sysRoleDataScopeJpaDao;

    @Autowired
    private SysRoleUserJpaDao sysRoleUserJpaDao;

    @Override
    public SysRoleDTO findDTOById(Long id) {
        SysRoleEntity entity = super.findOne(id);
        SysRoleDTO dto = new SysRoleDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setMenuIdList(sysRoleMenuJpaDao.findMenuIdByRoleId(id));
        dto.setDeptIdList(sysRoleDataScopeJpaDao.findDeptIdByRoleId(id));

        return dto;
    }

    @Override
    public SysRoleEntity save(SysRoleDTO dto, Long userId) {
        SysRoleEntity entity = new SysRoleEntity();

        BeanUtils.copyProperties(dto, entity);

        boolean update = null != dto.getId();

        if(update){
            entity.setCoverageUpdate(true);
        }else{
            if(null == entity.getDeptId()){
                entity.setDeptId(0L);
            }
            entity.setCreator(userId);
        }

        entity.setUpdater(userId);

        SysRoleEntity result = save(entity);

        List<Long> menuIdList = dto.getMenuIdList();

        if(!CollectionUtils.isEmpty(menuIdList)){
            if(update){
                sysRoleMenuJpaDao.deleteByRoleId(dto.getId());
            }

            List<SysRoleMenuEntity> roleMenuEntities = new ArrayList<>();

            menuIdList.forEach(menuId -> {
                SysRoleMenuEntity roleMenuEntity = new SysRoleMenuEntity();

                roleMenuEntity.setId(sequence.getSequence());
                roleMenuEntity.setRoleId(entity.getId());
                roleMenuEntity.setMenuId(menuId);
                roleMenuEntity.setCreator(userId);

                roleMenuEntities.add(roleMenuEntity);
            });

            sysRoleMenuJpaDao.saveAll(roleMenuEntities);
        }

        List<Long> deptIdList = dto.getDeptIdList();

        if(!CollectionUtils.isEmpty(deptIdList)){
            if(update){
                sysRoleDataScopeJpaDao.deleteByRoleId(dto.getId());
            }
            List<SysRoleDataScopeEntity> roleDataScopeEntities = new ArrayList<>();

            deptIdList.forEach(deptId -> {
                SysRoleDataScopeEntity roleDataScopeEntity = new SysRoleDataScopeEntity();

                roleDataScopeEntity.setId(sequence.getSequence());
                roleDataScopeEntity.setRoleId(entity.getId());
                roleDataScopeEntity.setDeptId(deptId);
                roleDataScopeEntity.setCreator(userId);

                roleDataScopeEntities.add(roleDataScopeEntity);
            });

            sysRoleDataScopeJpaDao.saveAll(roleDataScopeEntities);
        }

        return result;
    }

    @Override
    public Long findRoleIdByUserId(Long userId) {
        return sysRoleUserJpaDao.findRoleIdByUserId(userId);
    }

    @Override
    public void delete(Long id) {
        sysRoleMenuJpaDao.deleteByRoleId(id);

        sysRoleDataScopeJpaDao.deleteByRoleId(id);

        super.delete(id);
    }
}
