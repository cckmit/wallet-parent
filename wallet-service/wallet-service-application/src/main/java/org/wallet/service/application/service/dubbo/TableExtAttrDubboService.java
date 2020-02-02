package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.TableExtAttrCache;
import org.wallet.common.constants.field.TableExtAttrField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.TableExtAttrDTO;
import org.wallet.common.entity.TableExtAttrEntity;
import org.wallet.common.enums.TableExtAttrTypeEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.TableExtAttrService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_TABLE_EXT_ATTR)
public class TableExtAttrDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    TableExtAttrService tableExtAttrService;

    public ServiceResponse getTableExtAttrById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(TableExtAttrField.TABLE_EXT_ATTR_ID);
        }

        return response.setResult(tableExtAttrService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllTableExtAttr(ServiceRequest request, ServiceResponse response) {
        List<TableExtAttrDTO> dtoList = cache.get(TableExtAttrCache.CACHE_PREFIX, TableExtAttrCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<TableExtAttrEntity> appChainList = tableExtAttrService.findAll(new Sort(Sort.Direction.ASC, TableExtAttrField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            TableExtAttrDTO dto = new TableExtAttrDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(TableExtAttrCache.CACHE_PREFIX, TableExtAttrCache.ALL, dtoList, TableExtAttrCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findTableExtAttr(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findTableExtAttrPage(request, response); }

        search = null == search ? new Search() : search;

        List<TableExtAttrEntity> appChainList = tableExtAttrService.findAll(search);

        List<TableExtAttrDTO> dtoList = appChainList.stream().map(entity -> {
            TableExtAttrDTO dto = new TableExtAttrDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findTableExtAttrPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(TableExtAttrField.PAGE); }

        PageDTO<TableExtAttrEntity> entityPage = tableExtAttrService.findPage(search);

        List<TableExtAttrEntity> appVersionList = entityPage.getRecords();

        List<TableExtAttrDTO> dtoList = appVersionList.stream().map(entity -> {
            TableExtAttrDTO dto = new TableExtAttrDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addTableExtAttr(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        TableExtAttrEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(TableExtAttrField.USER_ID); }
        if(null == entity){ return Responses.missingParam(TableExtAttrField.TABLE_EXT_ATTR); }

        entity.setId(null);
        entity.setCreator(userId);
        entity.setUpdater(userId);
        if(null == entity.getType()){ entity.setType(TableExtAttrTypeEnum.TEXT); }
        if(null == entity.getRequired()){ entity.setRequired(Boolean.FALSE); }
        if(null == entity.getSort()){ entity.setSort(0); }
        if(null == entity.getEnable()){ entity.setEnable(Boolean.TRUE); }

        TableExtAttrEntity result = tableExtAttrService.save(entity);

        if(null == result){ return Responses.fail(ResponseCode.DATA_DUPLICATE, String.format("数据[%s:%s][%s:%s][%s:%s]已存在，不能重复添加",
                TableExtAttrField.APP_CHAIN_ID, entity.getChainId(),
                TableExtAttrField.DOMAIN, entity.getDomain(),
                TableExtAttrField.NAME, entity.getName()
                )); }

        return response.setResult(result);
    }

    public ServiceResponse updateTableExtAttr(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        TableExtAttrEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(TableExtAttrField.USER_ID); }
        if(null == entity){ return Responses.missingParam(TableExtAttrField.TABLE_EXT_ATTR); }
        if(null == entity.getId()){ return Responses.missingParam(TableExtAttrField.TABLE_EXT_ATTR_ID); }

        entity.setCoverageUpdate(true);
        entity.setUpdater(userId);

        TableExtAttrEntity result = tableExtAttrService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteTableExtAttr(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(TableExtAttrField.TABLE_EXT_ATTR_ID); }

        tableExtAttrService.delete(id);

        return response;
    }
}
