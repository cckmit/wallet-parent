package org.wallet.gateway.client.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wallet.dap.common.boot.SpringBootstrap;

@SpringBootApplication(scanBasePackages = "org.wallet")
public class ServiceStarter {
	public static void main(String[] args) {
		SpringBootstrap.run(ServiceStarter.class, args);
	}
}
