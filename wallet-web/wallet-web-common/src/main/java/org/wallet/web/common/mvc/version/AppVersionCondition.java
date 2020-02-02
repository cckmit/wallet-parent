package org.wallet.web.common.mvc.version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.wallet.common.enums.Device;
import org.wallet.common.enums.ResultCode;
import org.wallet.web.common.utils.ContextUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengfucheng
 **/
public class AppVersionCondition implements RequestCondition<AppVersionCondition> {
    private final static Logger log = LoggerFactory.getLogger(AppVersionCondition.class);
    private final static String UA_APP_VERSION = "Wallet/";
    private final static String HEADER_USER_AGENT = "user-agent";
    private final static String HEADER_APP_VERSION = "appVersion";
    private final static String HEADER_DEVICE = "device";
    private final static String POINT = ".";
    private final static String DIGITAL_REG = "\\d+";
    private final static MatchModelDTO ALL_IOS_MODEL = new MatchModelDTO(Device.IOS, MatchType.ALL, 0);
    private final static MatchModelDTO ALL_ANDROID_MODEL = new MatchModelDTO(Device.ANDROID, MatchType.ALL, 0);
    private final static MatchModelDTO ALL_UNKNOWN_MODEL = new MatchModelDTO(Device.UNKNOWN, MatchType.ALL, 0);
    private Map<Device, MatchModelDTO> modelMap = new HashMap<>(2);

    public AppVersionCondition(Map<Device, MatchModelDTO> modelMap) {
        this.modelMap = modelMap;
    }

    public AppVersionCondition(AppVersion appVersion) {
        if(null == appVersion){
            modelMap.put(Device.IOS, ALL_IOS_MODEL);
            modelMap.put(Device.ANDROID, ALL_ANDROID_MODEL);
            modelMap.put(Device.UNKNOWN, ALL_UNKNOWN_MODEL);
        } else {
            MatchModel[] models = appVersion.rules();
            for (MatchModel model : models) {
                modelMap.put(model.device(), new MatchModelDTO(model.device(), model.matchType(), model.appVersion()));
            }
        }
    }

    @Override
    public AppVersionCondition combine(AppVersionCondition other) {
        return new AppVersionCondition(other.getModelMap());
    }

    @Override
    public AppVersionCondition getMatchingCondition(HttpServletRequest request) {
        Device device = getDevice(request);
        Integer appVersion = getAppVersion(request);

        if(null == device){
            device = Device.UNKNOWN;
        }
        if(null == appVersion){
            appVersion = 0;
        }

        String userId = ContextUtil.getUserId(request);
        String uri = request.getRequestURI();

        if(modelMap.containsKey(device)){
            MatchModelDTO model = modelMap.get(device);
            if(match(userId, uri, model, appVersion)){
                return this;
            }
        }else{
            log.warn("拦截用户[{}]设备[{}]版本[{}]访问版本控制接口[{}]", userId, device, appVersion, uri);
        }
        return null;
    }

    @Override
    public int compareTo(AppVersionCondition other, HttpServletRequest request) {
        return 0;
    }

    public Map<Device, MatchModelDTO> getModelMap() {
        return modelMap;
    }

    /**
     * 获取客户端类型
     * @return 客户端类型
     */
    private Device getDevice(HttpServletRequest request){
        String device = request.getHeader(HEADER_DEVICE);

        if(!StringUtils.isEmpty(device)){
            if(Device.ANDROID.name().equals(device.toUpperCase())){
                return Device.ANDROID;
            }else{
                return Device.IOS;
            }
        }
        return null;
    }

    /**
     * 获取inChat 客户端版本号
     * @return 客户端版本号
     */
    private Integer getAppVersion(HttpServletRequest request){
        String appVersion = request.getHeader(HEADER_APP_VERSION);

        String userAgent = request.getHeader(HEADER_USER_AGENT);

        if(StringUtils.isEmpty(appVersion)
                && !StringUtils.isEmpty(userAgent)
                && userAgent.contains(UA_APP_VERSION)){
            appVersion = userAgent.substring(userAgent.indexOf(UA_APP_VERSION) + UA_APP_VERSION.length());
        }

        if(!StringUtils.isEmpty(appVersion)){
            if(appVersion.contains(POINT)){
                appVersion = appVersion.replaceAll("[" + POINT + "]", "");
            }
            if(appVersion.matches(DIGITAL_REG)){
                return Integer.parseInt(appVersion);
            }
        }

        return null;
    }

    /**
     * 是否匹配
     *
     * @param userId
     * @param uri
     * @param model 匹配模型
     * @param appVersion 请求中的APP版本号
     * @return 匹配结果
     */
    private boolean match(String userId, String uri, MatchModelDTO model, Integer appVersion){
        if(null == model || null == appVersion){
            return false;
        }
        if(MatchType.ALL.equals(model.getMatchType())){
            return true;
        }

        boolean lessThan = MatchType.LESS_THAN.equals(model.getMatchType()) && appVersion < model.getAppVersion();
        boolean moreThan = MatchType.MORE_THAN.equals(model.getMatchType()) && appVersion > model.getAppVersion();
        boolean equal = MatchType.EQUAL.equals(model.getMatchType()) && appVersion.equals(model.getAppVersion());

        boolean isMatch = lessThan || moreThan || equal;

        ResultCode resultCode;

        if(!isMatch){
            String code;
            if(MatchType.LESS_THAN.equals(model.getMatchType())){
                resultCode = ResultCode.AppVersionHigh;
            }else if(MatchType.MORE_THAN.equals(model.getMatchType())){
                resultCode = ResultCode.AppVersionLow;
            }else{
                resultCode = ResultCode.AppVersionNotEqual;
            }

            log.warn("拦截用户[{}]设备[{}]版本[{}]访问版本控制接口[{}][{}][{}]", userId,
                    model.getDevice(), appVersion, uri, model.getMatchType(),
                    model.getAppVersion());

            throw new AppVersionException(resultCode, model, appVersion);
        }
        return true;
    }
}
