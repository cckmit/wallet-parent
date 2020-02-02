package org.wallet.gateway.client.utils.httpclient;

/**
 * 伪造浏览器接口
 * @author zengfucheng
 **/
public interface Browser extends HttpPoint {
    String getOS();
    String getName();
    String getVersion();
}
