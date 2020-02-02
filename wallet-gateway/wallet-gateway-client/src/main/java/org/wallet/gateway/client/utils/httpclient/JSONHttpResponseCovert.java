package org.wallet.gateway.client.utils.httpclient;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;

/**
 * HTTP响应转JSON转换器
 * @author zengfucheng
 */
public class JSONHttpResponseCovert<T> implements HttpResponseCovert<T> {
    private static final HttpResponseCovert SELF = new JSONHttpResponseCovert<>();

    @SuppressWarnings("unchecked")
    public static <E> HttpResponseCovert<E> getDefault(){
        return SELF;
    }

    @Override
    public T covertResponse(HttpResponse response, String responseString, Class<T> clazz) {
        return JSON.parseObject(responseString, clazz);
    }
}
