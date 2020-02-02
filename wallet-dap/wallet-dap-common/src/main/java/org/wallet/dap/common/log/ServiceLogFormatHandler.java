package org.wallet.dap.common.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dubbo 服务日志格式化处理器
 * @author zengfucheng
 **/
public class ServiceLogFormatHandler {
    private Logger logger = LoggerFactory.getLogger(ServiceLogFormatHandler.class);
    private final static ServiceLogFormatHandler SELF = new ServiceLogFormatHandler();
    public static ServiceLogFormatHandler getInstance(){
        return SELF;
    }
    private Map<String, ServiceLogConfig> logConfigMap = new HashMap<>();

    /**
     * 获取日志格式化配置
     * @param methodName 接口名称
     * @return 日志格式化配置对象
     */
    public ServiceLogConfig getLogConfig(String methodName){
        return logConfigMap.get(methodName);
    }

    /**
     * 批量添加日志格式化配置
     * @param configList 日志格式化配置集合
     */
    public void addLogConfig(List<ServiceLogConfig> configList){
        if(!CollectionUtils.isEmpty(configList)){
            configList.forEach(this::addLogConfig);
        }
    }

    /**
     * 添加日志格式化配置
     * @param logConfig 日志格式化配置
     */
    public ServiceLogConfig addLogConfig(ServiceLogConfig logConfig){
        if(null == logConfig){
            return null;
        }
        if(!StringUtils.isEmpty(logConfig.getMethodName())){
            logConfigMap.put(logConfig.getMethodName(), logConfig);
        }
        return logConfig;
    }

    /**
     * 添加日志格式化配置
     * @param logConfigJSONString 日志格式化配置JSON 字符串
     */
    public ServiceLogConfig addLogConfig(String logConfigJSONString){
        if(!StringUtils.isEmpty(logConfigJSONString)){
            try {
                ServiceLogConfig logConfig = JSON.parseObject(logConfigJSONString, ServiceLogConfig.class);
                return addLogConfig(logConfig);
            }catch (JSONException e){
                logger.error("添加服务日志格式化配置失败：" + e.getMessage());
            }
        }
        return null;
    }
}
