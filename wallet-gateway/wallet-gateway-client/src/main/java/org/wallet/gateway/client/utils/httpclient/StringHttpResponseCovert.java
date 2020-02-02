package org.wallet.gateway.client.utils.httpclient;

import org.apache.http.HttpResponse;

/**
 * HTTP响应转String转换器
 * @author zengfucheng
 */
public class StringHttpResponseCovert implements HttpResponseCovert<String> {
    private static final StringHttpResponseCovert SELF = new StringHttpResponseCovert();

    public static StringHttpResponseCovert getDefault(){
        return SELF;
    }

    @Override
    public String covertResponse(HttpResponse response, String responseString, Class<String> clazz) {
        return responseString;
    }
}
