package org.wallet.gateway.client.utils.oss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wallet.dap.common.utils.EnvironmentUtil;

public class AvatarDispose {

    private static final Logger log = LoggerFactory.getLogger(AvatarDispose.class);

    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

    /**
     * url 加工处理
     *
     * @param url
     * @return
     */
    public static String getUrlHeadPathDispose(String url) {
        if (url == null) {
            return null;
        }
        //如果 URL 包含，https:// Or http:// 直接返回。
        if (url.toLowerCase().contains(HTTPS) || url.toLowerCase().contains(HTTP)) {
            return url;
        }
        //组装：https//+相对路径。
        return getHeadPath() + url;
    }

    private static String getHeadPath() {
        try {
            return EnvironmentUtil.getProperty("oss.headPath");
        } catch (Exception e) {
            log.error("OssConfig.properties file load error:{}", e.getMessage(), e);
            return "https://wallet.oss-cn-hongkong.aliyuncs.com/";
        }
    }
}
