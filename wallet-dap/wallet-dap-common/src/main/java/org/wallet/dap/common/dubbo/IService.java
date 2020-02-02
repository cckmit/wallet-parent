package org.wallet.dap.common.dubbo;

public interface IService {
	String PROVIDER_FILTER_NAME = "providerLogFilter";
	String CONSUMER_FILTER_NAME = "consumerLogFilter";

    /**
     * 执行方法
     * @param request 服务请求
     * @return 服务响应
     */
	ServiceResponse invoke(ServiceRequest request);
}
