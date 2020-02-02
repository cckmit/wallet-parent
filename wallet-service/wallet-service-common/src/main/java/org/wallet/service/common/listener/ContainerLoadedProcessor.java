package org.wallet.service.common.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;

/**
 * Spring Boot容器启动后会按照level方法的返回值按从大到小的顺序
 * 执行所有实现了ContainerLoadedProcessor类的handle方法。
 */
public interface ContainerLoadedProcessor {
	int level();
	void initializedHandle(ApplicationReadyEvent event);
}
