package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.entity.application.AppVisitLogEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
public interface AppVisitLogJpaDao extends BaseRepository<AppVisitLogEntity> {
    @Query(value = "select date_format(a.create_date, '%Y-%m-%d') date, count(a.id) num " +
            "from app_log_visit a " +
            "where a.chain_id = ?1 " +
            "and a.app_name like concat('%', ?2, '%') " +
            "and a.create_date >= ?3 " +
            "and a.create_date < ?4 " +
            "group by date_format(a.create_date, '%Y-%m-%d') " +
            "order by date", nativeQuery = true)
    List<Map<String, Object>> appVisitCount(Long chainId, String appName, Date startDate, Date endDate);

    @Query(value = "select a.app_name name, count(a.id) amount " +
            "from app_log_visit a " +
            "where a.chain_id = ?1 " +
            "and a.app_name like concat('%', ?2, '%') " +
            "and a.create_date >= ?3 " +
            "and a.create_date < ?4 " +
            "group by a.app_name " +
            "order by amount desc", nativeQuery = true)
    List<Map<String, Object>> appVisitRank(Long chainId, String appName, Date startDate, Date endDate);
}
