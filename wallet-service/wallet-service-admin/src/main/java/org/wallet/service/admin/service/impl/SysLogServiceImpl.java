package org.wallet.service.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.field.EntityField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.admin.SysErrorLogDTO;
import org.wallet.common.dto.admin.SysLoginLogDTO;
import org.wallet.common.dto.admin.SysOperationLogDTO;
import org.wallet.common.entity.admin.SysErrorLogEntity;
import org.wallet.common.entity.admin.SysLoginLogEntity;
import org.wallet.common.entity.admin.SysOperationLogEntity;
import org.wallet.common.enums.admin.OperationStatusEnum;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.SearchFinal;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ISequence;
import org.wallet.service.admin.dao.SysErrorLogJpaDao;
import org.wallet.service.admin.dao.SysLoginLogJpaDao;
import org.wallet.service.admin.dao.SysOperationLogJpaDao;
import org.wallet.service.admin.service.SysLogService;
import org.wallet.service.common.bind.JPACovert;
import org.wallet.service.common.bind.specification.MySpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class SysLogServiceImpl implements SysLogService {

    @Reference(group = DubboServiceGroup.DAP_SEQUENCE)
    private ISequence sequence;

    @Autowired
    private SysErrorLogJpaDao errorLogJpaDao;

    @Autowired
    private SysLoginLogJpaDao loginLogJpaDao;

    @Autowired
    private SysOperationLogJpaDao operationLogJpaDao;

    @Override
    public SysErrorLogEntity saveError(SysErrorLogDTO errorLog) {
        SysErrorLogEntity entity = new SysErrorLogEntity();
        BeanUtil.copyProperties(errorLog, entity);
        if(StringUtils.isEmpty(entity.getRequestParams())){
            entity.setRequestParams(EntityField.EMPTY);
        }
        if(null == entity.getCreator()){
            entity.setCreator(EntityField.DEFAULT_ADMIN_USER_ID);
        }
        entity.setId(sequence.getSequence());
        return errorLogJpaDao.save(entity);
    }

    @Override
    public SysLoginLogEntity saveLogin(SysLoginLogDTO loginLog) {
        SysLoginLogEntity entity = new SysLoginLogEntity();
        BeanUtil.copyProperties(loginLog, entity);
        entity.setId(sequence.getSequence());
        return loginLogJpaDao.save(entity);
    }

    @Override
    public SysOperationLogEntity saveOperation(SysOperationLogDTO operationLog) {
        SysOperationLogEntity entity = new SysOperationLogEntity();
        BeanUtil.copyProperties(operationLog, entity);
        entity.setId(sequence.getSequence());
        if(null == entity.getStatus()){
            entity.setStatus(OperationStatusEnum.FAIL.value());
        }
        if(null == entity.getOperation()){
            entity.setOperation(EntityField.EMPTY);
        }
        return operationLogJpaDao.save(entity);
    }

    @Override
    public PageDTO<SysOperationLogDTO> findOperationPage(Search search) {
        if (null == search.getPage()) { search.setPage(PageDTO.of(SearchFinal.DEFAULT_PAGE, SearchFinal.DEFAULT_LIMIT)); }
        if (null == search.getSort()) { search.setSort(SortDTO.by(SortDTO.Direction.DESC, SearchFinal.DEFAULT_SORT)); }
        Pageable pageable = JPACovert.covertPage(search.getPage(), search.getSort());
        Page<SysOperationLogEntity> page = operationLogJpaDao.findAll(new MySpecification<SysOperationLogEntity>(search).toPredicate(), pageable);
        List<SysOperationLogDTO> dtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(page.getContent())){
            page.getContent().forEach(entity -> {
                SysOperationLogDTO dto = new SysOperationLogDTO();

                BeanUtils.copyProperties(entity, dto);

                dtoList.add(dto);
            });
        }
        return new PageDTO<>(pageable.getPageNumber() + 1, pageable.getPageSize(), page.getTotalPages(), page.getTotalElements(), dtoList);

    }

    @Override
    public PageDTO<SysLoginLogDTO> findLoginPage(Search search) {
        if (null == search.getPage()) { search.setPage(PageDTO.of(SearchFinal.DEFAULT_PAGE, SearchFinal.DEFAULT_LIMIT)); }
        if (null == search.getSort()) { search.setSort(SortDTO.by(SortDTO.Direction.DESC, SearchFinal.DEFAULT_SORT)); }
        Pageable pageable = JPACovert.covertPage(search.getPage(), search.getSort());
        Page<SysLoginLogEntity> page = loginLogJpaDao.findAll(new MySpecification<SysLoginLogEntity>(search).toPredicate(), pageable);
        List<SysLoginLogDTO> dtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(page.getContent())){
            page.getContent().forEach(entity -> {
                SysLoginLogDTO dto = new SysLoginLogDTO();

                BeanUtils.copyProperties(entity, dto);

                dtoList.add(dto);
            });
        }
        return new PageDTO<>(pageable.getPageNumber() + 1, pageable.getPageSize(), page.getTotalPages(), page.getTotalElements(), dtoList);
    }
}
