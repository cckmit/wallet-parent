package org.wallet.dap.common.dubbo;

/**
 * @author zengfucheng
 */
public class Responses {

	public static ServiceResponse success(){
        return ServiceResponse.newInstance();
    }

	public static ServiceResponse success(Object result){
        ServiceResponse response = ServiceResponse.newInstance();
        return response.setResult(result);
    }

	public static ServiceResponse fail(String code, String msg){
        ServiceResponse response = ServiceResponse.newInstance();
        response.setRespCode(code);
        response.setRespMsg(msg);
        return response;
    }

	public static ServiceResponse fail(String msg){
        ServiceResponse response = ServiceResponse.newInstance();
        response.setRespCode(ResponseCode.FAIL);
        response.setRespMsg(msg);
        return response;
    }

	public static ServiceResponse illegalParam(String msg){
		ServiceResponse response = ServiceResponse.newInstance();
		response.setRespCode(ResponseCode.ILLEGAL_PARAM);
		response.setRespMsg(msg);
		return response;
    }

	public static ServiceResponse missingParam(String param){
	    return missingParam(ServiceResponse.newInstance(), param);
    }

	public static ServiceResponse missingParam(ServiceResponse response, String paramName){
	    response.setRespCode(ResponseCode.ILLEGAL_PARAM);
	    response.setRespMsg(String.format("参数[%s]不存在！", paramName));
	    return response;
    }

	public static ServiceResponse notFound(String methodName){
        ServiceResponse response = ServiceResponse.newInstance();
	    response.setRespCode(ResponseCode.NOT_FOUND);
	    response.setRespMsg(String.format("方法[%s]不存在！", methodName));
	    return response;
    }

	public static ServiceResponse notFoundData(Object id){
        ServiceResponse response = ServiceResponse.newInstance();
	    response.setRespCode(ResponseCode.NOT_FOUND_DATA);
	    response.setRespMsg(String.format("数据[%s]不存在！", id));
	    return response;
    }

	public static ServiceResponse notFoundData(String dataName, Object id){
        ServiceResponse response = ServiceResponse.newInstance();
	    response.setRespCode(ResponseCode.NOT_FOUND_DATA);
	    response.setRespMsg(String.format("数据[%s:%s]不存在！", dataName, id));
	    return response;
    }
}
