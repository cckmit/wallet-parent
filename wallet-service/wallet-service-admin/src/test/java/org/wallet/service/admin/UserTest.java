package org.wallet.service.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ISequence;
import org.wallet.service.admin.dao.SysUserJpaDao;

/**
 * @author zengfucheng
 **/
public class UserTest extends SpringBootJUnitTest {
    @Autowired
    SysUserJpaDao sysUserJpaDao;

    @Reference(group = DubboServiceGroup.DAP_SEQUENCE)
    ISequence sequence;

    @Test
    public void testAddUser(){
        SysUserEntity user = new SysUserEntity();
        user.setId(sequence.getSequence());
        user.setUsername("admin");
        sysUserJpaDao.save(user);
    }

    @Test
    public void testEditUser(){
        SysUserEntity user = sysUserJpaDao.findById(6560093971094437888L).get();

        user.setPassword("这是密码");

        sysUserJpaDao.save(user);
    }
}
