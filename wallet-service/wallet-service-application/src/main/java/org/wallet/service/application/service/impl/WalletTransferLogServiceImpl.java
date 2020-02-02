package org.wallet.service.application.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.chart.ChartDataDTO;
import org.wallet.common.dto.wallet.req.TransferStatisticsReqDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.wallet.WalletTransferLogEntity;
import org.wallet.service.application.dao.AppChainJpaDao;
import org.wallet.service.application.dao.WalletTransferLogJpaDao;
import org.wallet.service.application.service.WalletTransferLogService;
import org.wallet.service.common.service.AbstractCrudService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Service
public class WalletTransferLogServiceImpl extends AbstractCrudService<WalletTransferLogJpaDao, WalletTransferLogEntity> implements WalletTransferLogService {

    @Autowired
    private AppChainJpaDao appChainJpaDao;

    @Override
    public List<ChartDataDTO> transferStatistics(TransferStatisticsReqDTO reqDTO) {
        Date now = new Date(System.currentTimeMillis() + 100000);
        if(null == reqDTO.getStartDate()){
            reqDTO.setStartDate(new Date(0));
        }
        if(null == reqDTO.getEndDate()){
            reqDTO.setEndDate(now);
        }else{
            reqDTO.setEndDate(new Date(reqDTO.getEndDate().getTime() + Duration.ofDays(1).toMillis()));
        }

        AppChainEntity chainEntity = appChainJpaDao.getOne(reqDTO.getChainId());

        if(null == chainEntity){
            return null;
        }

        List<Map<String, Object>> mapList = getRepository().transferStatistics(chainEntity.getCoinName(), reqDTO.getStartDate(), reqDTO.getEndDate());

        List<ChartDataDTO> resultList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(mapList)){
            mapList.forEach(map -> {
                ChartDataDTO res = JSON.parseObject(JSON.toJSONString(map), ChartDataDTO.class);
                resultList.add(res);
            });
        }

        return resultList;
    }
}
