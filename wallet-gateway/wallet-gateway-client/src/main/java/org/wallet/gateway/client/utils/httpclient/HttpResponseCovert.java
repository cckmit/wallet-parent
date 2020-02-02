package org.wallet.gateway.client.utils.httpclient;

import org.apache.http.HttpResponse;

/**
 * HTTP 响应转换器，适用于HTTP连接池
 * @author zengfucheng
 */
public interface HttpResponseCovert<T> {

    /**
     * 转换响应
     * @param response Apache HTTP 响应
     * @param responseString 响应字符串
     * @param clazz 结果类模版
     * @return 结果
     */
    T covertResponse(HttpResponse response, String responseString, Class<T> clazz);

    /**
     * 根据类型获取对应结果转换器
     * @param clazz
     * @param <T>
     * @return
     */
    static <T> HttpResponseCovert<T> getByResultType(Class<T> clazz){
        return JSONHttpResponseCovert.getDefault();
    }
}
