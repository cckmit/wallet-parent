package org.wallet.service.batch;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.service.batch.service.WalletBlockTransService;

@Transactional
public class WalletBlockTransTest extends SpringBootJUnitTest{

    @Autowired
    private WalletBlockTransService walletBlockTransService;

    @Test
    @Rollback(value = false)
    public void fixAbnormalTrans() {
        walletBlockTransService.fixAbnormalTrans();
    }
}
