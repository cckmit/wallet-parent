package org.wallet.service.application.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
public interface WalletCoinOrderJpaDao extends BaseRepository<WalletCoinOrderEntity> {

    @Query(value = "select date_format(o.create_date, '%Y-%m-%d') date, count(o.id) num, sum(o.amount) amount " +
            "from wallet_coin_order o " +
            "where o.status = 'FINISH' " +
            "and o.coin_name = ?1 " +
            "and o.create_date >= ?2 " +
            "and o.create_date < ?3 " +
            "group by date_format(o.create_date, '%Y-%m-%d') " +
            "order by date", nativeQuery = true)
    List<Map<String, Object>> accountBuyFrequency(String coinName, Date startDate, Date endDate);

    @Query(value = "select o.payment_type name, count(o.id) num " +
            "from wallet_coin_order o " +
            "where o.status = 'FINISH' " +
            "and o.coin_name = ?1 " +
            "and o.create_date >= ?2 " +
            "and o.create_date < ?3 " +
            "group by o.payment_type " +
            "order by name", nativeQuery = true)
    List<Map<String, Object>> accountBuyPercent(String coinName, Date startDate, Date endDate);
}
