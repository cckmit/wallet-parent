package org.wallet.web.application.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.constants.field.AppAdvertField;
import org.wallet.common.constants.field.AppInfoField;
import org.wallet.common.constants.field.AppTypeField;
import org.wallet.common.constants.field.EntityField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.application.AppAdvertDTO;
import org.wallet.common.dto.application.AppTagDTO;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.common.enums.application.AppAdvertPointEnum;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.web.common.crypto.DecryptRequest;
import org.wallet.web.common.crypto.EncryptResponse;
import org.wallet.web.common.mvc.controller.BaseController;

import java.util.List;

/**
 * @author zengfucheng
 **/
@DecryptRequest
@EncryptResponse
@RestController
@RequestMapping("discovery")
public class DiscoveryController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_APP_ADVERT)
    private IService appAdvertService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_CHAIN)
    private IService appChainService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_INFO)
    private IService appInfoService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_TAG)
    private IService appTagService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_TYPE)
    private IService appTypeService;

    @GetMapping("chains")
    public SimpleResult chains(){
        Search search = Searchs.of(SearchFilters.eq(EntityField.ENABLE, Boolean.TRUE));

        search.setSort(SortDTO.asc(EntityField.SORT));

        ServiceResponse response = appChainService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_CHAIN, "findAppChain", search));

        return Results.by(response);
    }

    @GetMapping("{chainId}/advert/top")
    public SimpleResult advertTop(@PathVariable Long chainId){
        Search search = Searchs.of(
                SearchFilters.eq(AppAdvertField.ENABLE, Boolean.TRUE),
                SearchFilters.eq(AppAdvertField.APP_CHAIN_ID, chainId),
                SearchFilters.eq(AppAdvertField.POINT, AppAdvertPointEnum.DISCOVERY_TOP)
        );

        search.setSort(SortDTO.asc(EntityField.SORT));

        ServiceResponse response = appAdvertService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_TYPE, "findAppAdvert", search));

        List<AppAdvertDTO> advertList = response.getResult();

        if(!CollectionUtils.isEmpty(advertList)){
            advertList.forEach(advert -> {
                advert.setChainId(null);
                advert.removeAdminAttr();
            });
        }

        return Results.by(response);
    }

    @GetMapping("{chainId}/advert/middle")
    public SimpleResult advertMiddle(@PathVariable Long chainId){
        Search search = Searchs.of(
                SearchFilters.eq(AppAdvertField.ENABLE, Boolean.TRUE),
                SearchFilters.eq(AppAdvertField.APP_CHAIN_ID, chainId),
                SearchFilters.eq(AppAdvertField.POINT, AppAdvertPointEnum.DISCOVERY_MIDDLE)
        );

        search.setSort(SortDTO.asc(EntityField.SORT));

        ServiceResponse response = appAdvertService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_ADVERT, "findAppAdvert", search));

        List<AppAdvertDTO> advertList = response.getResult();

        if(!CollectionUtils.isEmpty(advertList)){
            advertList.forEach(advert -> {
                advert.setChainId(null);
                advert.removeAdminAttr();
            });
        }

        return Results.by(response);
    }

    @GetMapping("{chainId}/advert/dapp/summary")
    public SimpleResult advertDAppSummary(@PathVariable Long chainId){
        Search search = Searchs.of(
                SearchFilters.eq(AppAdvertField.ENABLE, Boolean.TRUE),
                SearchFilters.eq(AppAdvertField.APP_CHAIN_ID, chainId),
                SearchFilters.eq(AppAdvertField.POINT, AppAdvertPointEnum.APP_HOME_TOP)
        );

        search.setSort(SortDTO.asc(EntityField.SORT));

        ServiceResponse response = appAdvertService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_ADVERT, "findAppAdvert", search));

        List<AppAdvertDTO> advertList = response.getResult();

        if(!CollectionUtils.isEmpty(advertList)){
            advertList.forEach(advert -> {
                advert.setChainId(null);
                advert.removeAdminAttr();
            });
        }

        return Results.by(response);
    }

    @GetMapping("{chainId}/types")
    public SimpleResult types(@PathVariable Long chainId){
        Search search = Searchs.of(SearchFilters.eq(AppInfoField.ENABLE, Boolean.TRUE), SearchFilters.eq(AppTypeField.APP_CHAIN_ID, chainId));

        search.setSort(SortDTO.asc(EntityField.SORT));

        ServiceResponse response = appTypeService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_ADVERT, "findAppType", search));

        return Results.by(response);
    }

    @GetMapping("{chainId}/dapp/single")
    public SimpleResult singleDApp(@PathVariable Long chainId,
                                   @RequestParam(value = "name") String name){
        Search search = Searchs.of(
                SearchFilters.eq(AppInfoField.APP_CHAIN_ID, chainId),
                SearchFilters.eq(AppInfoField.ENABLE, Boolean.TRUE),
                SearchFilters.eq(AppInfoField.NAME, name)
        );

        ServiceResponse response = appInfoService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_TAG, "findAppInfo", search));

        List<AppInfoEntity> appInfoList = response.getResult();

        if(!CollectionUtils.isEmpty(appInfoList)){
            return Results.success(appInfoList.get(0));
        }else{
            return Results.success();
        }
    }

    @GetMapping("{chainId}/dapp")
    public SimpleResult searchDApp(@PathVariable Long chainId,
                                   @RequestParam(value = "typeId", required = false) Long typeId,
                                   @RequestParam(value = "name", required = false) String name,
                                   PageDTO pageDTO){
        Search search = Searchs.of(
                SearchFilters.eq(AppInfoField.APP_CHAIN_ID, chainId),
                SearchFilters.eq(AppInfoField.ENABLE, Boolean.TRUE)
        );

        if(null != typeId){
            search.addSearchFilter(SearchFilters.eq(AppInfoField.APP_TYPE_ID, typeId));
        }

        if(!StringUtils.isEmpty(name)){
            search.addSearchFilter(SearchFilters.like(AppInfoField.NAME, name));
        }

        Boolean hasPage = null != pageDTO && null != pageDTO.getPageNo() && null != pageDTO.getPageSize();
        if(hasPage){
            search.setPage(pageDTO);
        }

        search.setSort(SortDTO.asc(AppInfoField.APP_TYPE_ID, AppInfoField.SORT));

        ServiceResponse response = appInfoService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_TAG, "findAppInfo", search));

        return Results.by(response);
    }

    @GetMapping("{chainId}/dapp/featured")
    public SimpleResult featuredDApps(@PathVariable Long chainId,
                                      @RequestParam(value = "typeId", required = false) Long typeId){
        AppTagDTO tag = new AppTagDTO();
        tag.setChainId(chainId);
        tag.setTypeId(typeId);
        tag.setTag(AppTagEnum.FEATURED);

        ServiceResponse response = appInfoService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_TAG, "findAppByTag", tag));

        return Results.by(response);
    }

    @GetMapping("{chainId}/dapp/hot")
    public SimpleResult hotDApps(@PathVariable Long chainId){
        AppTagDTO tag = new AppTagDTO();
        tag.setChainId(chainId);
        tag.setTag(AppTagEnum.HOT);

        ServiceResponse response = appInfoService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_TAG, "findAppByTag", tag));

        return Results.by(response);
    }

    @GetMapping("{chainId}/dapp/hot_search")
    public SimpleResult hotSearchDApps(@PathVariable Long chainId){
        AppTagDTO tag = new AppTagDTO();
        tag.setChainId(chainId);
        tag.setTag(AppTagEnum.HOTSPOT);

        ServiceResponse response = appInfoService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_TAG, "findAppByTag", tag));

        return Results.by(response);
    }

    @GetMapping("{chainId}/dapp/recommend")
    public SimpleResult recommendDApps(@PathVariable Long chainId){
        AppTagDTO tag = new AppTagDTO();
        tag.setChainId(chainId);
        tag.setTag(AppTagEnum.RECOMMEND);

        ServiceResponse response = appInfoService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_TAG, "findAppByTag", tag));

        return Results.by(response);
    }

    @GetMapping("{chainId}/type/recommend")
    public SimpleResult typeRecommendDApps(@PathVariable Long chainId){
        ServiceResponse response = appInfoService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_INFO, "findTypeRecommendApp", chainId));

        return Results.by(response);
    }
}
