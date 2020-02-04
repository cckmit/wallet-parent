package org.wallet.dap.common.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

public class SpringBootstrap extends AbstractBootstrap {

    private ConfigurableApplicationContext context;

    private static SpringBootstrap instance;

    private SpringBootstrap() {
    }

    public static SpringBootstrap getInstance() {
        if (instance == null) {
            synchronized (SpringBootstrap.class) {
                if (instance == null) {
                    instance = new SpringBootstrap();
                }
            }
        }
        return instance;
    }

    public static void run(Class<?> clazz, String[] args) {
        SpringBootstrap boot = SpringBootstrap.getInstance();
        boot.setClazz(clazz);
        boot.setArgs(args);
        if (args.length == 0) {
            SpringBootstrap.getInstance().start();
        } else {
            switch (args.length) {
                case 1:
                    if ("start".equals(args[0])) {
                        SpringBootstrap.getInstance().start();
                    } else {
                        SpringBootstrap.getInstance().sendClosingSignal();
                    }
                    break;
                case 2:
                    SpringBootstrap.getInstance().setServiceName(args[0]);
                    if ("start".equals(args[1])) {
                        SpringBootstrap.getInstance().start();
                    } else {
                        SpringBootstrap.getInstance().sendClosingSignal();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void startContainers(Class<?> clazz, String[] args) {
        context = SpringApplication.run(clazz, args);
    }

    @Override
    protected void closeContainers() {
        if (!Objects.isNull(context)) {
            context.stop();
            context.close();
        }
    }

}
