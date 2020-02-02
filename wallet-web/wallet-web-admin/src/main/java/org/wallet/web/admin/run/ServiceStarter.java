package org.wallet.web.admin.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wallet.dap.common.boot.SpringBootstrap;

/**
 * @author zengfucheng
 */
@SpringBootApplication(scanBasePackages = "org.wallet")
public class ServiceStarter {
    public static void main(String[] args) {
        SpringBootstrap.run(ServiceStarter.class, args);
    }
}
