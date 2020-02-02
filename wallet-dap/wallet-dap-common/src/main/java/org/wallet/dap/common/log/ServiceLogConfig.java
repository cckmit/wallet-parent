package org.wallet.dap.common.log;

import java.util.Arrays;
import java.util.List;

/**
 * Dubbo 服务调用日志格式化配置
 * @author zengfucheng
 **/
public class ServiceLogConfig {
    public static final String REQUEST_PARAM_NAME = "params";
    public static final String RESPONSE_RESULT_NAME = "result";
    /**
     * 接口名称
     */
    private String methodName;
    /**
     * 该接口是否输出日志（默认所有接口都会输出）
     */
    private boolean noPrint;
    /**
     * 是否排除请求参数日志输出（如请求参数字节数过大）
     */
    private boolean noParam;
    /**
     * 是否排除响应结果日志输出（如请求参数字节数过大）
     */
    private boolean noResult;
    /**
     * 请求参数格式化类型集合
     */
    private List<LogFormatEntity> paramFormatList;
    /**
     * 响应结果格式化类型集合
     */
    private List<LogFormatEntity> resultFormatList;

    public ServiceLogConfig() {
    }

    public ServiceLogConfig(String methodName) {
        this.methodName = methodName;
    }

    public ServiceLogConfig(String methodName, boolean noParam) {
        this.methodName = methodName;
        this.noParam = noParam;
    }

    public ServiceLogConfig(String methodName, boolean noParam, boolean noResult) {
        this.methodName = methodName;
        this.noParam = noParam;
        this.noResult = noResult;
    }

    public ServiceLogConfig(String methodName, boolean noParam, boolean noResult, List<LogFormatEntity> resultFormatList) {
        this.methodName = methodName;
        this.noParam = noParam;
        this.noResult = noResult;
        this.resultFormatList = resultFormatList;
    }

    public ServiceLogConfig(String methodName, LogFormatEntity... resultFormatArray) {
        this.methodName = methodName;
        this.resultFormatList = Arrays.asList(resultFormatArray);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isNoPrint() {
        return noPrint;
    }

    public void setNoPrint(boolean noPrint) {
        this.noPrint = noPrint;
    }

    public boolean isNoParam() {
        return noParam;
    }

    public void setNoParam(boolean noParam) {
        this.noParam = noParam;
    }

    public boolean isNoResult() {
        return noResult;
    }

    public void setNoResult(boolean noResult) {
        this.noResult = noResult;
    }

    public List<LogFormatEntity> getParamFormatList() {
        return paramFormatList;
    }

    public void setParamFormatList(List<LogFormatEntity> paramFormatList) {
        this.paramFormatList = paramFormatList;
    }

    public List<LogFormatEntity> getResultFormatList() {
        return resultFormatList;
    }

    public void setResultFormatList(List<LogFormatEntity> resultFormatList) {
        this.resultFormatList = resultFormatList;
    }
}
