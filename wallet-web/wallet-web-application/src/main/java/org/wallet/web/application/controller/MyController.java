package org.wallet.web.application.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.common.constants.field.AppAdvertField;
import org.wallet.common.constants.field.AppLinkField;
import org.wallet.common.constants.field.AppMessageField;
import org.wallet.common.constants.field.EntityField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.application.AppAdvertDTO;
import org.wallet.common.enums.application.AppAdvertPointEnum;
import org.wallet.common.enums.application.AppLinkTypeEnum;
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
@RequestMapping("my")
public class MyController extends BaseController {

    @Reference(group = DubboServiceGroup.SERVICE_APP_ADVERT)
    private IService appAdvertService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_CHAIN_NODE)
    private IService appChainNodeService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_LINK)
    private IService appLinkService;

    @Reference(group = DubboServiceGroup.SERVICE_APP_MESSAGE)
    private IService appMessageService;

    @Reference(group = DubboServiceGroup.SERVICE_WALLET_INFO)
    private IService walletInfoService;

    @GetMapping("wallet/info")
    public SimpleResult walletInfo(){
        ServiceResponse response = walletInfoService.invoke(createRequest(DubboServiceGroup.SERVICE_WALLET_INFO, "getCurrentWalletInfo"));

        return Results.by(response);
    }

    @GetMapping("chain/{chainId}/node")
    public SimpleResult chainNode(@PathVariable Long chainId){
        Search search = Searchs.of(
                SearchFilters.eq(AppAdvertField.ENABLE, Boolean.TRUE),
                SearchFilters.eq(AppAdvertField.APP_CHAIN_ID, chainId)
        );

        search.setSort(SortDTO.asc(EntityField.SORT));

        ServiceResponse response = appChainNodeService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_CHAIN_NODE, "findAppChainNode", search));

        return Results.by(response);
    }

    @GetMapping("message/system")
    public SimpleResult systemMessage(PageDTO pageDTO){
        Search search = new Search();

        search.setSort(SortDTO.desc(AppMessageField.CREATE_DATE));

        if(null == pageDTO.getPageNo()){
            pageDTO.setPageNo(Search.DEFAULT_PAGE);
        }
        if(null == pageDTO.getPageSize()){
            pageDTO.setPageSize(Search.DEFAULT_LIMIT);
        }
        search.setPage(pageDTO);

        ServiceResponse response = appMessageService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_MESSAGE, "findAppMessage", search));

        return Results.by(response);
    }

    @GetMapping("advert/recommend")
    public SimpleResult recommendPoster(){
        Search search = Searchs.of(
                SearchFilters.eq(AppAdvertField.ENABLE, Boolean.TRUE),
                SearchFilters.eq(AppAdvertField.POINT, AppAdvertPointEnum.RECOMMEND_POSTER)
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

    @GetMapping("advert/splash-screen")
    public SimpleResult splashScreen(){
        Search search = Searchs.of(
                SearchFilters.eq(AppAdvertField.ENABLE, Boolean.TRUE),
                SearchFilters.eq(AppAdvertField.POINT, AppAdvertPointEnum.SPLASH_SCREEN)
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

    @GetMapping("about_us")
    public SimpleResult aboutUs(PageDTO pageDTO){
        Search search = Searchs.of(SearchFilters.eq(AppLinkField.TYPE, AppLinkTypeEnum.NAVIGATION));

        search.addOrSearchFilter(SearchFilters.eq(AppLinkField.TYPE, AppLinkTypeEnum.VERSION_UPDATE));

        search.setSort(SortDTO.asc(AppLinkField.SORT));

        if(null != pageDTO.getPageNo() && null != pageDTO.getPageSize()){
            search.setPage(pageDTO);
        }

        ServiceResponse response = appLinkService.invoke(createRequest(DubboServiceGroup.SERVICE_APP_LINK, "findAppLink", search));

        return Results.by(response);
    }
}
