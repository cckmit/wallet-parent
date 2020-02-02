package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wallet.common.entity.oss.OssResult;
import org.wallet.dap.common.dubbo.*;
import org.wallet.dap.common.utils.EnvironmentUtil;
import org.wallet.gateway.client.utils.oss.OssUploadUtil;
import org.wallet.gateway.client.utils.oss.StsUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Aliyun OSS 资源上传。
 * @author zengfucheng
 */
@Service(group = DubboServiceGroup.CLIENT_OSS)
@org.springframework.stereotype.Service
public class OssService extends BaseDubboService implements IService {
    private static Logger logger = LoggerFactory.getLogger(OssService.class);

    public ServiceResponse getOssSignature(ServiceRequest request, ServiceResponse response) {
        try {
        	String dir = request.getParamValue("dir");
        	if(dir != null){
        		 Map<String, String> stsTokenMap = StsUtil.getPolicyMap(dir);
                 response.setRespCode(ResponseCode.SUCCESS);
                 response.setResultValue("data", stsTokenMap);
        	}
        } catch (Exception e) {
            response.setRespCode(ResponseCode.SYS_ERROR);
            logger.error("oss service error info ", e);
        }
        return response;
    }

    public ServiceResponse getOssStsTokenService(ServiceRequest request, ServiceResponse response) {
        try {
            Map<String, Object> stsTokenMap = StsUtil.getStsTokenMap();
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("data", stsTokenMap);
        } catch (Exception e) {
            response.setRespCode(ResponseCode.SYS_ERROR);
            logger.error("oss service getOssStsTokenService error info ", e);
        }
        return response;
    }

    public ServiceResponse getOssStsToken(ServiceRequest request, ServiceResponse response) {
        try {
            String token = StsUtil.getStsToken();
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("data", token);
        } catch (Exception e) {
            response.setRespCode(ResponseCode.SYS_ERROR);
            logger.error("oss service getOssStsToken error info ", e);
        }
        return response;
    }

    /**
     * 获取OOS文件头部固定路径
     * @return
     */
    public ServiceResponse headPath(ServiceRequest request, ServiceResponse response) {
        try {
            StringBuffer path = new StringBuffer("https://");
            path.append(EnvironmentUtil.getProperty("oss.bucketName")).append(".");
            path.append(EnvironmentUtil.getProperty("oss.name"));
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("data", path);
            logger.debug("oss headPath:"+path.toString());
        } catch (Exception e) {
            response.setRespCode(ResponseCode.SYS_ERROR);
            logger.error("oss service headPath error info ", e);
        }
        return response;
    }

    /**
     * 单个文件上传文件 OSS 中
     */
    public ServiceResponse upload(ServiceRequest request, ServiceResponse response) {
        try {
            File uploadFile = request.getParamValue("param");
            //上传到OSS 服务器。
            OssResult ossResult = OssUploadUtil.uploadFile(uploadFile);
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("data", ossResult);
        } catch (Exception e) {
            OssResult ossResultError = new OssResult();
            ossResultError.setStatus(false);
            response.setRespCode(ResponseCode.SYS_ERROR);
            response.setResultValue("data", ossResultError);
            logger.error("oss service upload error info ", e);
        }
        return response;
    }

    /**
     * 单个文件上传文件 OSS 中
     */
    @SuppressWarnings("unchecked")
    public ServiceResponse uploadFile(ServiceRequest request, ServiceResponse response) {
        List<OssResult> listResult = new ArrayList<>(9);
        try {
            IdentityHashMap<String,Object> map = (IdentityHashMap) request.getParams().get("param");

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                InputStream inputStream = new ByteArrayInputStream((byte[]) entry.getValue());
                String name=entry.getKey();
                OssResult ossResult = OssUploadUtil.uploadFile(inputStream,name);
                listResult.add(ossResult);
            }
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("data", listResult);
        } catch (Exception e) {
            logger.error("OSS file update error:{}",e.toString(),e);
            OssResult ossResultError = new OssResult();
            ossResultError.setStatus(false);
            response.setRespCode(ResponseCode.SYS_ERROR);
            response.setResultValue("data", ossResultError);
        }
        return response;
    }

}
