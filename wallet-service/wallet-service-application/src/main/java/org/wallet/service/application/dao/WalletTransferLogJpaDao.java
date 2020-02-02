package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.entity.wallet.WalletTransferLogEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
public interface WalletTransferLogJpaDao extends BaseRepository<WalletTransferLogEntity> {

    @Query(value = "select date_format(log.create_date, '%Y-%m-%d') date, count(log.id) num, sum(log.usd_amount) amount " +
            "from wallet_log_transfer log " +
            "where log.token = ?1 " +
            "and log.create_date >= ?2 " +
            "and log.create_date < ?3 " +
            "group by date_format(log.create_date, '%Y-%m-%d')" +
            "order by date", nativeQuery = true)
    List<Map<String, Object>> transferStatistics(String coinName, Date startDate, Date endDate);
}
