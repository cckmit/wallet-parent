package org.wallet.service.batch.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wallet.common.constants.cache.WalletCoinOrderCache;
import org.wallet.dap.cache.lock.DapLock;
import org.wallet.service.batch.service.WalletBlockTransService;

/**
 * @author zengfucheng
 **/
@Slf4j
@Component
public class WalletCoinOrderFixTask {

    @Autowired
    private WalletBlockTransService walletBlockTransService;

    @Scheduled(cron = "0 * * * * *")
    public void fix(){
        DapLock lock = new DapLock(WalletCoinOrderCache.KEY_FIX_ORDER, 30);
        boolean locked = false;
        try{
            locked = lock.tryLock();
            if(locked){
                walletBlockTransService.fixAbnormalTrans();
            }else{
                log.warn("正在修复...");
            }
        } catch (Exception e){
            log.error("修复EOS异常交易失败：{}", e.getMessage());
        } finally {
            if(locked){
                lock.unlock();
            }
        }
    }
}
