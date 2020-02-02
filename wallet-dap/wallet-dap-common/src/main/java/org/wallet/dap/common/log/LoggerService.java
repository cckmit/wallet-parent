package org.wallet.dap.common.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.wallet.dap.common.dubbo.ResponseCode;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

public class LoggerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerService.class);

	public static ServiceResponse print(ServiceRequest request, ServiceResponse response) {
		response.setRespTime(System.currentTimeMillis() - request.getReqTime());
		String methodName = request.getMethodName();
		ServiceLogConfig logConfig = ServiceLogFormatHandler.getInstance().getLogConfig(methodName);
		String reqLogString;
		String resLogString;
		if(null != logConfig){
			if(logConfig.isNoPrint()){
				return response;
			}
			SerializeFilter[] reqSerFilter = null;
			if(logConfig.isNoParam()){
				SimplePropertyPreFilter reqParamFilter = new SimplePropertyPreFilter(ServiceRequest.class);
				reqParamFilter.getExcludes().add(ServiceLogConfig.REQUEST_PARAM_NAME);
				reqSerFilter = new SerializeFilter[]{reqParamFilter};
			}else if(!CollectionUtils.isEmpty(logConfig.getParamFormatList())){
				List<LogFormatEntity> entityList = logConfig.getParamFormatList();
				List<SimplePropertyPreFilter> filterList = new ArrayList<>(entityList.size());
				entityList.forEach(entity -> filterList.add(entity.covertToFilter()));
				reqSerFilter = filterList.toArray(new SerializeFilter[0]);
			}
			reqLogString = JSON.toJSONString(request, reqSerFilter);

			SerializeFilter[] resSerFilter = null;
			if(logConfig.isNoResult()){
				SimplePropertyPreFilter resResultFilter = new SimplePropertyPreFilter(ServiceResponse.class);
				resResultFilter.getExcludes().add(ServiceLogConfig.RESPONSE_RESULT_NAME);
				resSerFilter = new SerializeFilter[]{resResultFilter};
			}else if(!CollectionUtils.isEmpty(logConfig.getResultFormatList())){
				List<LogFormatEntity> entityList = logConfig.getResultFormatList();
				List<SimplePropertyPreFilter> filterList = new ArrayList<>(entityList.size());
				entityList.forEach(entity -> filterList.add(entity.covertToFilter()));
				resSerFilter = filterList.toArray(new SerializeFilter[0]);
			}
			resLogString = JSON.toJSONString(response, resSerFilter);
		}else{
			reqLogString = JSON.toJSONString(request);
			resLogString = JSON.toJSONString(response);
		}
		LOGGER.info("[Req:{} | Resp:{}]", reqLogString, resLogString);
		return response;
	}
	
	public static ServiceResponse print(ServiceRequest request, ServiceResponse response, Throwable e) {
		response.setRespTime(System.currentTimeMillis() - request.getReqTime());
		response.setRespCode(ResponseCode.SYS_ERROR);
		response.setRespMsg(e.getMessage());
		LOGGER.error("[Req:{} | Resp:{}]",JSON.toJSONString(request), JSON.toJSONString(response), e);
		return response;
	}

}
