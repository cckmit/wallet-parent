package org.wallet.gateway.client.utils.httpclient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * 伪造Safari 浏览器
 * @author zengfucheng
 **/
public class SafariBrowser extends HttpBeforePoint implements Browser{
    private static final String NAME = "Safari";
    private static final String OS = "Macintosh; U; Intel Mac OS X 10_6_8; en-us";
    private static final String VERSION = "534.50";
    private String os;
    private String version;

    public SafariBrowser() {
        os = OS;
        version = VERSION;
    }

    public SafariBrowser(String os, String version) {
        this.os = os;
        this.version = version;
    }

    @Override
    public Object beforeExecute(HttpClient client, HttpUriRequest request) {
        request.setHeader("Language", "zh_CN");
        request.setHeader("User-Agent", "Mozilla/5.0 (" + getOS() + ") AppleWebKit/537.36 (KHTML, like Gecko) " + getName() + "/" + getVersion());
        return null;
    }

    @Override
    public String getOS() {
        return os;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getVersion() {
        return version;
    }
}
