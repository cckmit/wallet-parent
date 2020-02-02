package org.wallet.dap.common.dubbo;

import lombok.Data;
import org.wallet.dap.common.utils.HostUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务响应
 * @author zengfucheng
 */
@Data
public class ServiceResponse implements Serializable{
	private static final long serialVersionUID = -885151682463012042L;
	/**
	 * 响应代码
	 */
	private String respCode;
	/**
	 * 响应消息
	 */
	private String respMsg;
	/**
	 * 响应时间
	 */
	private long respTime;
	/**
	 * 服务器IP
	 */
	private final String serverIp;
	/**
	 * 响应结果
	 */
	private Map<String, Object> result;
	
	public static ServiceResponse newInstance() {
		return new ServiceResponse();
	}

    private ServiceResponse() {
        result = new HashMap<>();
        serverIp = HostUtil.getHostIp();
		respCode = ResponseCode.SUCCESS;
		respMsg = "ok";
    }

    public ServiceResponse setResultValue(String key, Object value) {
        result.put(key, value);
        return this;
	}

	@SuppressWarnings("unchecked")
    public <T> T getResultValue(String key) {
        return (T) result.get(key);
    }

    public ServiceResponse setResult(Object value){
	    result.put("result", value);
	    return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getResult(){
        if(result.size() == 1){
            return (T) result.get(result.keySet().iterator().next());
        }else{
            return (T) result;
        }
	}

	public boolean success(){
		return ResponseCode.SUCCESS.equals(respCode);
	}
}
