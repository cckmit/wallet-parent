package org.wallet.service.application.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.dto.application.req.FindAppVisitLogReqDTO;
import org.wallet.common.dto.chart.ChartDataDTO;
import org.wallet.common.dto.chart.RankDataDTO;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.common.entity.application.AppVisitLogEntity;
import org.wallet.service.application.dao.AppChainJpaDao;
import org.wallet.service.application.dao.AppVisitLogJpaDao;
import org.wallet.service.application.service.AppVisitLogService;
import org.wallet.service.common.service.AbstractCrudService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zengfucheng
 **/
@Service
public class AppVisitLogServiceImpl extends AbstractCrudService<AppVisitLogJpaDao, AppVisitLogEntity> implements AppVisitLogService {

    @Autowired
    private AppChainJpaDao appChainJpaDao;

    @Override
    public List<ChartDataDTO> appVisitCount(FindAppVisitLogReqDTO reqDTO) {
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

        String appName = reqDTO.getAppName();

        appName = StringUtils.isEmpty(appName) ? "" : appName;

        List<Map<String, Object>> mapList = getRepository().appVisitCount(reqDTO.getChainId(), appName, reqDTO.getStartDate(), reqDTO.getEndDate());

        List<ChartDataDTO> resultList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(mapList)){
            mapList.forEach(map -> {
                ChartDataDTO res = JSON.parseObject(JSON.toJSONString(map), ChartDataDTO.class);

                resultList.add(res);
            });
        }

        return resultList;
    }

    @Override
    public List<RankDataDTO> appVisitRank(FindAppVisitLogReqDTO reqDTO) {
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

        String appName = reqDTO.getAppName();

        appName = StringUtils.isEmpty(appName) ? "" : appName;

        List<Map<String, Object>> mapList = getRepository().appVisitRank(reqDTO.getChainId(), appName, reqDTO.getStartDate(), reqDTO.getEndDate());

        List<RankDataDTO> resultList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(mapList)){
            AtomicInteger no = new AtomicInteger(0);
            mapList.forEach(map -> {
                RankDataDTO res = JSON.parseObject(JSON.toJSONString(map), RankDataDTO.class);

                res.setNo(no.addAndGet(1));

                resultList.add(res);
            });
        }

        return resultList;
    }
}
