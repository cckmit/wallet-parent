package org.wallet.web.common.mvc.version;

import org.wallet.common.enums.Device;

/**
 * @author zengfucheng
 **/
public class MatchModelDTO {

    /**
     * 匹配设备类型
     * @return 设备类型
     */
    private Device device;

    /**
     * 匹配类型
     * @return 匹配类型
     */
    private MatchType matchType;

    /**
     * 客户端版本号
     * @return 客户端版本号
     */
    private Integer appVersion;

    public MatchModelDTO() {
    }

    public MatchModelDTO(Device device, MatchType matchType, Integer appVersion) {
        this.device = device;
        this.matchType = matchType;
        this.appVersion = appVersion;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }
}
