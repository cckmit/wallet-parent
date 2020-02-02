package org.wallet.gateway.client.utils.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * Http连接池请求切点
 * @author zengfucheng
 */
public interface HttpPoint {
    /**
     * 请求前切点
     * @param client 请求实体
     * @param request 请求对象
     * @return 返回的对象将在afterExecute中传入
     */
    Object beforeExecute(HttpClient client, HttpUriRequest request);

    /**
     * 请求后切点
     * @param beforeReturnValue 响应结果
     * @param response 响应结果
     * @param responseString 响应结果字符串
     */
    void afterExecute(Object beforeReturnValue, HttpResponse response, String responseString);

    /**
     * 异常切点
     * @param throwable
     */
    void onException(Throwable throwable);
}
