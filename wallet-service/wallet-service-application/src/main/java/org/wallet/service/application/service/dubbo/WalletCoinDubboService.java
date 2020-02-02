package org.wallet.service.application.service.dubbo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.AppID;
import org.wallet.common.constants.cache.WalletCoinCache;
import org.wallet.common.constants.field.TableExtAttrValueField;
import org.wallet.common.constants.field.WalletCoinField;
import org.wallet.common.constants.field.WalletCoinOrderField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.TableExtAttrValueDTO;
import org.wallet.common.dto.block.BlockResult;
import org.wallet.common.dto.block.req.CreateAccountReqDTO;
import org.wallet.common.dto.wallet.WalletCoinDTO;
import org.wallet.common.dto.wallet.req.CreateCoinAccountReqDTO;
import org.wallet.common.dto.wallet.req.FindCoinInfoReqDTO;
import org.wallet.common.entity.TableExtAttrEntity;
import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.common.entity.wallet.WalletCoinEntity;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.common.enums.BusinessDomainEnum;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.dubbo.*;
import org.wallet.service.application.service.*;
import org.wallet.service.common.service.dubbo.BaseDubboService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service
@com.alibaba.dubbo.config.annotation.Service(group = DubboServiceGroup.SERVICE_WALLET_COIN, timeout = 30000)
public class WalletCoinDubboService extends BaseDubboService implements IService{

    @Autowired
    Cache cache;

    @Autowired
    private TableExtAttrService tableExtAttrService;

    @Autowired
    private TableExtAttrValueService tableExtAttrValueService;

    @Autowired
    private WalletCoinService walletCoinService;

    @Autowired
    private WalletCoinOrderService walletCoinOrderService;

    @Autowired
    private AppInviteCodeService appInviteCodeService;

    @Reference(group = DubboServiceGroup.CLIENT_BLOCK, timeout = 30000)
    private IService blockService;

    public ServiceResponse createCoinAccount(ServiceRequest request, ServiceResponse response) {
        CreateCoinAccountReqDTO dto = request.getParam();
        String code = dto.getInviteCode();

        WalletCoinOrderEntity orderEntity = walletCoinOrderService.findOne(Searchs.of(SearchFilters.eq(WalletCoinOrderField.INVITE_CODE, code)));

        if(null == orderEntity){
            return Responses.illegalParam(String.format("未找到对应邀请码[%s]的订单", code));
        }

        response = appInviteCodeService.useCode(code, orderEntity.getId());

        if(!response.success()){ return response; }

        AppInviteCodeEntity codeEntity = response.getResult();

        CreateAccountReqDTO reqDTO = new CreateAccountReqDTO();

        BeanUtils.copyProperties(dto, reqDTO);

        reqDTO.setName(dto.getAccountName());

        response = blockService.invoke(ServiceRequest.newInstance(AppID.WALLET_SERVICE_APPLICATION, DubboServiceGroup.CLIENT_BLOCK,
                "createAccount", null, reqDTO));

        BlockResult result = response.getResult();

        if(!response.success() || null == result || !result.success()){
            appInviteCodeService.rollbackCodeStatus(code);
        }
        return response;
    }

    public ServiceResponse getWalletCoinById(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();
        if(null == id){ return Responses.missingParam(WalletCoinField.WALLET_COIN_ID); }

        return response.setResult(walletCoinService.findDTOById(id));
    }

    @SuppressWarnings("unchecked")
    public ServiceResponse findAllWalletCoin(ServiceRequest request, ServiceResponse response) {
        List<WalletCoinDTO> dtoList = cache.get(WalletCoinCache.CACHE_PREFIX, WalletCoinCache.ALL, List.class);

        if(null != dtoList){ return response.setResult(dtoList); }

        List<WalletCoinEntity> appChainList = walletCoinService.findAll(new Sort(Sort.Direction.ASC, WalletCoinField.SORT));

        dtoList = appChainList.stream().map(entity -> {
            WalletCoinDTO dto = new WalletCoinDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        cache.put(WalletCoinCache.CACHE_PREFIX, WalletCoinCache.ALL, dtoList, WalletCoinCache.EXPIRE);

        return response.setResult(dtoList);
    }

    public ServiceResponse findWalletCoin(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null != search.getPage()){ return findWalletCoinPage(request, response); }

        search = null == search ? new Search() : search;

        List<WalletCoinEntity> appChainList = walletCoinService.findAll(search);

        List<WalletCoinDTO> dtoList = appChainList.stream().map(entity -> {
            WalletCoinDTO dto = new WalletCoinDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(dtoList);
    }

    public ServiceResponse findCoinInfo(ServiceRequest request, ServiceResponse response) {
        FindCoinInfoReqDTO findCoinInfoReqDTO = request.getParam();

        if(null == findCoinInfoReqDTO){
            return response;
        }

        if(StringUtils.isEmpty(findCoinInfoReqDTO.getCoinName())){ return Responses.missingParam(WalletCoinField.NAME); }

        return response.setResult(walletCoinService.findCoinInfo(findCoinInfoReqDTO));
    }

    public ServiceResponse findCoinBaseInfo(ServiceRequest request, ServiceResponse response) {
        FindCoinInfoReqDTO findCoinInfoReqDTO = request.getParam();

        if(null == findCoinInfoReqDTO){
            return response;
        }

        if(StringUtils.isEmpty(findCoinInfoReqDTO.getChainId())){ return Responses.missingParam(WalletCoinField.APP_CHAIN_ID); }

        return response.setResult(walletCoinService.findCoinBaseInfo(findCoinInfoReqDTO));
    }

    public ServiceResponse findWalletCoinPage(ServiceRequest request, ServiceResponse response) {
        Search search = request.getParam();

        if(null == search || null == search.getPage()){ Responses.missingParam(WalletCoinField.PAGE); }

        PageDTO<WalletCoinEntity> entityPage = walletCoinService.findPage(search);

        List<WalletCoinEntity> appVersionList = entityPage.getRecords();

        List<WalletCoinDTO> dtoList = appVersionList.stream().map(entity -> {
            WalletCoinDTO dto = new WalletCoinDTO();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

        return response.setResult(PageDTO.of(entityPage, dtoList));
    }

    public ServiceResponse addWalletCoin(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletCoinDTO dto = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletCoinField.USER_ID); }
        if(null == dto){ return Responses.missingParam(WalletCoinField.WALLET_COIN); }

        dto.sync();

        ServiceResponse checkResponse = checkCoinAttr(dto, userId, false);

        if(!checkResponse.success()){ return checkResponse;}

        if(null == dto.getDecimals()){ dto.setDecimals(4);}

        return response.setResult(walletCoinService.save(dto, userId));
    }

    public ServiceResponse updateWalletCoin(ServiceRequest request, ServiceResponse response) {
        Long userId = request.getUserId();
        WalletCoinDTO dto = request.getParam();

        if(null == userId){ return Responses.missingParam(WalletCoinField.USER_ID); }
        if(null == dto){ return Responses.missingParam(WalletCoinField.WALLET_COIN); }
        if(null == dto.getId()){ return Responses.missingParam(WalletCoinField.WALLET_COIN_ID); }

        dto.sync();

        WalletCoinEntity entity = walletCoinService.findOne(dto.getId());

        if(null == entity){ Responses.notFoundData(dto.getId()); }

        dto.setChainId(entity.getChainId());

        ServiceResponse checkResponse = checkCoinAttr(dto, userId, true);

        if(!checkResponse.success()){ return checkResponse;}

        return response.setResult(walletCoinService.save(dto, userId));
    }

    public ServiceResponse deleteWalletCoin(ServiceRequest request, ServiceResponse response) {
        Long id = request.getParam();

        if(null == id){ return Responses.missingParam(WalletCoinField.WALLET_COIN_ID); }

        tableExtAttrValueService.deleteBy(Searchs.of(SearchFilters.eq(TableExtAttrValueField.DATA_ID, id)));

        walletCoinService.delete(id);

        return response;
    }

    public ServiceResponse checkCoinAttr(WalletCoinDTO dto, Long userId, Boolean allowEmptyValue){
        List<TableExtAttrEntity> baseAttrs = tableExtAttrService.findAll(Searchs.of(
                SortDTO.asc(TableExtAttrValueField.SORT),
                SearchFilters.eq(WalletCoinField.APP_CHAIN_ID, dto.getChainId()),
                SearchFilters.eq(WalletCoinField.DOMAIN, BusinessDomainEnum.COIN_BASE_INFO)
        ));

        if(!CollectionUtils.isEmpty(baseAttrs)){
            ServiceResponse checkResponse = checkAttrValue(userId, WalletCoinField.BASE_ATTRS, baseAttrs, dto.getBaseAttrs(), allowEmptyValue);

            if(!checkResponse.success()){ return checkResponse; }
        }else{
            dto.setBaseAttr(null);
            dto.setBaseAttrs(null);
        }

        return ServiceResponse.newInstance();
    }

    public ServiceResponse checkAttrValue(Long userId, String fieldName, List<TableExtAttrEntity> attrs, List<TableExtAttrValueDTO> attrValues, Boolean allowEmptyValue){
        ServiceResponse response = ServiceResponse.newInstance();

        if(CollectionUtils.isEmpty(attrs)){
            return response;
        }else if(CollectionUtils.isEmpty(attrValues) && allowEmptyValue){
            return response;
        }

        Map<String, TableExtAttrEntity> attrMap = attrs.stream().collect(
                Collectors.toMap(TableExtAttrEntity::getName, Function.identity())
        );
        Map<String, TableExtAttrValueDTO> valueMap = new HashMap<>();

        if(!CollectionUtils.isEmpty(attrValues)){
           valueMap = attrValues.stream().collect(
                   Collectors.toMap(TableExtAttrValueDTO::getName, Function.identity())
           );
        }

        Set<Map.Entry<String, TableExtAttrEntity>> entries = attrMap.entrySet();

        Date now = new Date();

        for(Map.Entry<String, TableExtAttrEntity> entry : entries){
            String name = entry.getKey();
            TableExtAttrEntity attr = entry.getValue();
            if(attr.getRequired() && !valueMap.containsKey(name)){
                return Responses.missingParam(fieldName + ">" + name);
            }
            TableExtAttrValueDTO value = valueMap.get(name);
            if(null != value){
                value.setDomain(attr.getDomain());
                value.setType(attr.getType());
                value.setAttrId(attr.getId());
                value.setSort(attr.getSort());
                value.setLabel(attr.getLabel());
                value.setCreator(userId);
                value.setCreateDate(now);
            }
        }

        return response;
    }
}
