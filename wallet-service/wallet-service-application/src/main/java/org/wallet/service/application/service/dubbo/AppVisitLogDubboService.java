package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.AppVisitLogCache;
import org.wallet.common.constants.cache.WalletCoinCache;
import org.wallet.common.constants.field.AppVisitLogField;
import org.wallet.common.constants.field.WalletCoinField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.application.AppVisitLogDTO;
import org.wallet.common.dto.application.req.FindAppVisitLogReqDTO;
import org.wallet.common.dto.wallet.req.VisitAppReqDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.common.entity.application.AppVisitLogEntity;
import org.wallet.common.entity.wallet.WalletCoinEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.AppChainService;
import org.wallet.service.application.service.AppInfoService;
import org.wallet.service.application.service.AppVisitLogService;
import org.wallet.service.application.service.WalletCoinService;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_APP_VISIT_LOG)
public class AppVisitLogDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    AppInfoService appInfoService;

    @Autowired
    AppChainService appChainService;

    @Autowired
    AppVisitLogService appVisitLogService;

    @Autowired
    WalletCoinService walletCoinService;

    public ServiceResponse appVisitCount(ServiceRequest request, ServiceResponse response) {
        FindAppVisitLogReqDTO req = request.getParam();
        if(null == req){
            return Responses.missingParam("req");
        }

        return response.setResult(appVisitLogService.appVisitCount(req));
    }

    public ServiceResponse appVisitRank(ServiceRequest request, ServiceResponse response) {
        FindAppVisitLogReqDTO req = request.getParam();
        if(null == req){
            return Responses.missingParam("req");
        }

        return response.setResult(appVisitLogService.appVisitRank(req));
    }

    public ServiceResponse getAppVisitLogById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){
            return Responses.missingParam(AppVisitLogField.APP_VISIT_LOG_ID);
        }

        return response.setResult(appVisitLogService.findOne(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllAppVisitLog(ServiceRequest request, ServiceResponse response) {
        List<AppVisitLogDTO> dtoList = cache.get(AppVisitLogCache.CACHE_PREFIX, AppVisitLogCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<AppVisitLogEntity> appChainList = appVisitLogService.findAll(new Sort(Sort.Direction.ASC, AppVisitLogField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            AppVisitLogDTO dto = new AppVisitLogDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(AppVisitLogCache.CACHE_PREFIX, AppVisitLogCache.ALL, dtoList, AppVisitLogCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppVisitLog(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findAppVisitLogPage(request, response); }

        search = null == search ? new Search() : search;

        List<AppVisitLogEntity> appChainList = appVisitLogService.findAll(search);

        List<AppVisitLogDTO> dtoList = appChainList.stream().map(entity -> {
            AppVisitLogDTO dto = new AppVisitLogDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findAppVisitLogPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(AppVisitLogField.PAGE); }

        PageDTO<AppVisitLogEntity> entityPage = appVisitLogService.findPage(search);

        List<AppVisitLogEntity> appVisitLogList = entityPage.getRecords();

        List<AppVisitLogDTO> dtoList = appVisitLogList.stream().map(entity -> {
            AppVisitLogDTO dto = new AppVisitLogDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addAppVisitLog(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppVisitLogEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppVisitLogField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppVisitLogField.APP_VISIT_LOG); }

        entity.setId(null);
        entity.setCreator(userId);

        AppVisitLogEntity result = appVisitLogService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse addVisitAppLog(ServiceRequest request, ServiceResponse response) {
        VisitAppReqDTO reqDTO = request.getParam();

        AppVisitLogEntity entity = new AppVisitLogEntity();

        AppInfoEntity appInfo = appInfoService.findOne(reqDTO.getAppId());

        if(null == appInfo){ return Responses.notFoundData(AppVisitLogField.APP_INFO_ID, reqDTO.getAppId()); }

        AppChainEntity appChain = appChainService.findOne(appInfo.getChainId());

        if(null == appChain){ return Responses.notFoundData(AppVisitLogField.APP_CHAIN_ID, appInfo.getChainId()); }

        entity.setAppId(reqDTO.getAppId());
        entity.setAppName(appInfo.getName());
        entity.setChainId(appInfo.getChainId());
        entity.setToken(appChain.getCoinName());
        entity.setAccount(reqDTO.getAccount());
        entity.setCoinName(reqDTO.getCoinName());
        entity.setCreator(AppVisitLogField.DEFAULT_ADMIN_USER_ID);

        AppVisitLogEntity result = appVisitLogService.save(entity);

        return response.setResult(result);
    }

    public ServiceResponse updateAppVisitLog(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        AppVisitLogEntity entity = request.getParam();

        if(null == userId){ return Responses.missingParam(AppVisitLogField.USER_ID); }
        if(null == entity){ return Responses.missingParam(AppVisitLogField.APP_VISIT_LOG); }
        if(null == entity.getId()){ return Responses.missingParam(AppVisitLogField.APP_VISIT_LOG_ID); }

        entity.setCoverageUpdate(true);

        AppVisitLogEntity result = appVisitLogService.save(entity);

        if(null == result){ return Responses.notFoundData(entity.getId()); }

        return response.setResult(result);
    }

    public ServiceResponse deleteAppVisitLog(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(AppVisitLogField.APP_VISIT_LOG_ID); }

        AppVisitLogEntity entity = appVisitLogService.findOne(id);

        appVisitLogService.delete(id);

        return response;
    }
}
