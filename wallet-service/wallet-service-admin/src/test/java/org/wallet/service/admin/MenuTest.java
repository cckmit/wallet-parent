package org.wallet.service.admin;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.service.admin.service.SysMenuService;

/**
 * @author zengfucheng
 **/
public class MenuTest extends SpringBootJUnitTest {
    @Autowired
    SysMenuService menuService;

    @Test
    public void testAddUser(){
        menuService.getUserPermissions(1L, true);
        menuService.getUserPermissions(2L, false);
    }

}
