package org.wallet.dap.common.dubbo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.wallet.dap.common.utils.HostUtil;
import org.wallet.dap.common.utils.ReqSeqNoGenerator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务请求对象
 * @author zengfucheng
 */
@Data
public class ServiceRequest implements Serializable{
	private static final long serialVersionUID = 306860174925786970L;
    /**
     * 应用ID
     */
    @JSONField(ordinal = 1)
    private String appId;
    /**
     * 请求接口名
     */
    @JSONField(ordinal = 2)
    private String methodName;
    /**
     * 用户ID
     */
    @JSONField(ordinal = 3)
    private Long userId;
    /**
     * 参数
     */
    @JSONField(ordinal = 4)
    private Map<String,Object> params;
    /**
     * 请求时间
     */
    @JSONField(ordinal = 5)
    private long reqTime;
    /**
     * 请求序列号
     */
    @JSONField(ordinal = 6)
	private final String reqSeqNo;
    /**
     * 业务跟踪序列号
     */
    @JSONField(ordinal = 7)
	private String bizTrackNo;
    /**
     * 服务器IP
     */
    @JSONField(ordinal = 8)
    private final String hostIp;
    /**
     * 渠道ID
     */
    @JSONField(ordinal = 9)
	private String channelId;
    /**
     * 接口服务标识
     */
    @JSONField(ordinal = 10)
	private String serviceId;
	
	public static ServiceRequest newInstance() {
		return new ServiceRequest();
	}

	public static ServiceRequest newInstance(String appId, String serviceId) {
		return new ServiceRequest(appId, serviceId, null, null, null);
	}

	public static ServiceRequest newInstance(String appId, String serviceId, String methodName) {
		return new ServiceRequest(appId, serviceId, methodName, null, null);
	}

	public static ServiceRequest newInstance(String appId, String serviceId, String methodName, Long userId) {
		return new ServiceRequest(appId, serviceId, methodName, userId, null);
	}

	public static ServiceRequest newInstance(String appId, String serviceId, String methodName, Long userId, Object param) {
		return new ServiceRequest(appId, serviceId, methodName, userId, param);
	}
	
	private ServiceRequest() {
		this.hostIp = HostUtil.getHostIp();
		this.reqSeqNo = ReqSeqNoGenerator.getReqSeqNo();
		this.reqTime = System.currentTimeMillis();
		this.params = new HashMap<>();
		this.bizTrackNo = this.reqSeqNo;
	}

	private ServiceRequest(String appId, String serviceId, String methodName, Long userId, Object param){
	    this();
	    this.appId = appId;
	    this.serviceId = serviceId;
	    this.methodName = methodName;
	    this.userId = userId;
	    if(param instanceof Map){
	        getParams().putAll((Map) param);
        }else{
            setParam(param);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getParamValue(String key) {
        return (T) params.get(key);
    }

    public ServiceRequest setParamValue(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public ServiceRequest setParam(Object value){
	    params.put("param", value);
        return this;
    }

    @SuppressWarnings("unchecked")
    @JSONField(serialize = false)
    public <T> T getParam(){
	    if(params.size() == 1){
            return (T) params.get(params.keySet().iterator().next());
        }else{
	        return null;
        }
    }
}
