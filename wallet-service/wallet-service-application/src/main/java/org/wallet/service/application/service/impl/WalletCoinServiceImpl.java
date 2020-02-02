package org.wallet.service.application.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.QuotesCache;
import org.wallet.common.constants.cache.WalletCoinCache;
import org.wallet.common.constants.cache.WalletPayConfigCache;
import org.wallet.common.constants.field.TableExtAttrValueField;
import org.wallet.common.constants.field.WalletCoinField;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.TableExtAttrValueDTO;
import org.wallet.common.dto.wallet.CoinAvgPriceDTO;
import org.wallet.common.dto.wallet.WalletCoinDTO;
import org.wallet.common.dto.wallet.req.FindCoinInfoReqDTO;
import org.wallet.common.entity.TableExtAttrValueEntity;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.wallet.WalletCoinEntity;
import org.wallet.common.enums.BusinessDomainEnum;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.utils.ThreadPool;
import org.wallet.service.application.dao.AppChainJpaDao;
import org.wallet.service.application.dao.WalletCoinJpaDao;
import org.wallet.service.application.service.TableExtAttrValueService;
import org.wallet.service.application.service.WalletCoinService;
import org.wallet.service.common.service.AbstractCrudService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Service
public class WalletCoinServiceImpl extends AbstractCrudService<WalletCoinJpaDao, WalletCoinEntity> implements WalletCoinService {

    @Autowired
    private TableExtAttrValueService tableExtAttrValueService;

    @Autowired
    private AppChainJpaDao appChainJpaDao;

    @PostConstruct
    public void initTypeCache(){
        List<AppChainEntity> chainEntityList = appChainJpaDao.findAll();

        if(!CollectionUtils.isEmpty(chainEntityList)){
            for (AppChainEntity chain : chainEntityList){
                List<WalletCoinEntity> coinEntityList = findAll(Searchs.of(
                        SortDTO.asc(WalletCoinField.SORT),
                        SearchFilters.eq(WalletCoinField.APP_CHAIN_ID, chain.getId()),
                        SearchFilters.eq(WalletCoinField.ENABLE, Boolean.TRUE)
                ));
                if(!CollectionUtils.isEmpty(coinEntityList)){
                    List<String> coinList = new ArrayList<>();
                    coinEntityList.forEach(coin -> coinList.add(coin.getName() + WalletCoinCache.SEP + coin.getContractAddress()));
                    logger.info("更新主链[{}]币种缓存: {}", chain.getCoinName(), JSON.toJSONString(coinList));
                    cache.put(WalletCoinCache.CACHE_PREFIX, chain.getCoinName(), coinList);
                }
            }
        }
    }

    @Override
    public void deleteCustomCache() {
        List<AppChainEntity> chainEntityList = appChainJpaDao.findAll();

        if(!CollectionUtils.isEmpty(chainEntityList)){
            for (AppChainEntity chain : chainEntityList){
                cache.evict(WalletPayConfigCache.CACHE_PREFIX, chain.getCoinName());
            }
        }

        ThreadPool.getInstance().exe(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException ignored) {
            }
            initTypeCache();
        });
    }

    @Override
    public WalletCoinDTO findDTOById(Long id) {

        WalletCoinEntity entity = super.findOne(id);
        WalletCoinDTO dto = new WalletCoinDTO();

        BeanUtils.copyProperties(entity, dto);

        buildExtAttr(dto);

        return dto;
    }

    @Override
    public List<WalletCoinDTO> findCoinInfo(FindCoinInfoReqDTO findCoinInfoReqDTO) {
        Long chainId = findCoinInfoReqDTO.getChainId();
        String coinName = findCoinInfoReqDTO.getCoinName();
        String contract = findCoinInfoReqDTO.getContract();
        Search search = Searchs.of(SortDTO.asc(WalletCoinField.SORT),
                SearchFilters.eq(WalletCoinField.NAME, coinName));

        if(null != chainId){
            search.addSearchFilter(SearchFilters.eq(WalletCoinField.APP_CHAIN_ID, chainId));
        }

        List<WalletCoinEntity> entityList = findAll(search);

        List<WalletCoinDTO> dtoList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(entityList)){
            entityList.forEach(entity -> {
                WalletCoinDTO dto = new WalletCoinDTO();

                BeanUtils.copyProperties(entity, dto);

                if(!StringUtils.isEmpty(contract) && !contract.equals(dto.getContractAddress())){
                    return;
                }

                buildExtAttr(dto);

                dtoList.add(dto);
            });
        }

        return dtoList;
    }

    @Override
    public List<WalletCoinDTO> findCoinBaseInfo(FindCoinInfoReqDTO findCoinInfoReqDTO) {
        List<WalletCoinEntity> entityList;

        if(CollectionUtils.isEmpty(findCoinInfoReqDTO.getCoinNames())){
            entityList = getRepository().findByChainIdAndEnableOrderBySort(findCoinInfoReqDTO.getChainId(), Boolean.TRUE);
        }else{
            entityList = getRepository().findByChainIdAndEnableAndNameInOrderBySort(findCoinInfoReqDTO.getChainId(), Boolean.TRUE, findCoinInfoReqDTO.getCoinNames());
        }

        List<WalletCoinDTO> dtoList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(entityList)){
            AppChainEntity chain = appChainJpaDao.getOne(findCoinInfoReqDTO.getChainId());

            entityList.forEach(entity -> {
                WalletCoinDTO dto = new WalletCoinDTO();

                BeanUtils.copyProperties(entity, dto);

                CoinAvgPriceDTO avgPriceDTO = getAvgPrice(chain.getCoinName(), dto.getContractAddress(), dto.getName());

                if(null != avgPriceDTO){
                    dto.setUsdRate(avgPriceDTO.getAvgUSDRate());
                }

                dtoList.add(dto);
            });
        }

        return dtoList;
    }

    private void buildExtAttr(WalletCoinDTO dto) {
        if(null == dto){
            return;
        }

        List<TableExtAttrValueEntity> baseAttrValues = tableExtAttrValueService.findAll(Searchs.of(
                SortDTO.asc(TableExtAttrValueField.SORT),
                SearchFilters.eq(TableExtAttrValueField.DOMAIN, BusinessDomainEnum.COIN_BASE_INFO),
                SearchFilters.eq(TableExtAttrValueField.DATA_ID, dto.getId())
        ));

        dto.setBaseAttrs(baseAttrValues.stream().map(value -> {
            TableExtAttrValueDTO valueDTO = new TableExtAttrValueDTO();
            BeanUtils.copyProperties(value, valueDTO);
            return valueDTO;
        }).collect(Collectors.toList()));

        dto.sync();
    }

    @Override
    public WalletCoinDTO save(WalletCoinDTO dto, Long userId) {
        WalletCoinEntity entity = new WalletCoinEntity();

        BeanUtils.copyProperties(dto, entity);

        boolean update = null != dto.getId();

        if(update){
            entity.setCoverageUpdate(true);

            if(!CollectionUtils.isEmpty(dto.getBaseAttr())){
                tableExtAttrValueService.deleteBy(Searchs.of(
                        SearchFilters.eq(TableExtAttrValueField.DOMAIN, BusinessDomainEnum.COIN_BASE_INFO),
                        SearchFilters.eq(TableExtAttrValueField.DATA_ID, dto.getId())
                ));
            }
        }else{
            if(null == entity.getSort()){ entity.setSort(0); }
            if(null == entity.getEnable()){ entity.setEnable(true); }
            entity.setCreator(userId);
        }
        entity.setUpdater(userId);

        WalletCoinEntity result = save(entity);

        if(!CollectionUtils.isEmpty(dto.getBaseAttr())){
            List<TableExtAttrValueEntity> baseAttrValueList = dto.getBaseAttrs().stream()
                    .map(TableExtAttrValueDTO::toEntity).collect(Collectors.toList());

            baseAttrValueList.forEach(value -> value.setDataId(result.getId()));

            tableExtAttrValueService.saveBatch(baseAttrValueList);
        }

        BeanUtils.copyProperties(result, dto);

        dto.setBaseAttr(TableExtAttrValueDTO.toMap(dto.getBaseAttrs()));
        dto.setBaseAttrs(dto.getBaseAttrs());

        return dto;
    }

    /**
     * 获取交易对各交易所平均行情价
     * @param chainCoin 主链币种
     * @param contract 币种合约
     * @param coin 币种
     * @return
     */
    private CoinAvgPriceDTO getAvgPrice(String chainCoin, String contract, String coin){
        String key = QuotesCache.AVG + QuotesCache.SEP + chainCoin + QuotesCache.SEP + contract;

        return cache.get(key, coin, CoinAvgPriceDTO.class);
    }
}
