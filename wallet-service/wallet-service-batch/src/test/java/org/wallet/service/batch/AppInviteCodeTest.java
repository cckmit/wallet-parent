package org.wallet.service.batch;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.common.enums.application.AppInviteCodeTypeEnum;
import org.wallet.service.batch.service.AppInviteCodeService;

@Transactional
public class AppInviteCodeTest extends SpringBootJUnitTest{

    @Autowired
    private AppInviteCodeService inviteCodeService;

    @Test
    @Rollback(value = false)
    public void getNewCode() {
        logger.info(JSON.toJSONString(inviteCodeService.createNewCode(AppInviteCodeTypeEnum.EOS_ACCOUNT)));
    }
}
