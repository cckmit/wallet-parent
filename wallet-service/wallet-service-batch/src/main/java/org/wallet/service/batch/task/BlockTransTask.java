package org.wallet.service.batch.task;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.AppID;
import org.wallet.common.constants.cache.WalletBlockTransCache;
import org.wallet.dap.cache.lock.DapLock;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.batch.service.WalletBlockTransService;

/**
 * @author zengfucheng
 **/
@Slf4j
@Component
public class BlockTransTask {

    @Reference(group = DubboServiceGroup.CLIENT_DFUSE)
    private IService dFuseService;

    @Autowired
    private WalletBlockTransService walletBlockTransService;

    @Scheduled(cron = "0/20 * * * * *")
    public void scanDeals(){
        DapLock lock = new DapLock(WalletBlockTransCache.KEY_SYNC_BLOCK_TRANS, 30);
        boolean locked = false;
        try{
            locked = lock.tryLock();
            if(locked){
                String eosReceiverAccount = walletBlockTransService.getEosReceiverAccount();
                if(StringUtils.isEmpty(eosReceiverAccount)){
                    log.warn("获取EOS收款账号失败");
                    return;
                }

                ServiceResponse response = dFuseService.invoke(ServiceRequest.newInstance(
                        AppID.WALLET_SERVICE_BATCH, DubboServiceGroup.CLIENT_DFUSE, "queryTrans", null, eosReceiverAccount));

                if(!response.success()){
                    return;
                }

                walletBlockTransService.syncTrans(response.getResult());
            }else{
                log.warn("正在同步...");
            }
        } catch (Exception e){
            log.error("同步EOS收款交易失败：{}", e.getMessage());
        } finally {
            if(locked){
                lock.unlock();
            }
        }
    }
}
