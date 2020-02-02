package org.wallet.gateway.client.utils.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @author zengfucheng
 **/
public abstract class HttpBeforePoint implements HttpPoint {
    @Override
    public abstract Object beforeExecute(HttpClient client, HttpUriRequest request);

    @Override
    public void afterExecute(Object beforeReturnValue, HttpResponse response, String responseString) {

    }

    @Override
    public void onException(Throwable throwable) {

    }
}
