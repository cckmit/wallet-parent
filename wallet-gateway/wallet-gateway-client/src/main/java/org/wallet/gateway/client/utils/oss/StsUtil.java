package org.wallet.gateway.client.utils.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wallet.dap.common.utils.EnvironmentUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StsUtil {

    private static Logger logger = LoggerFactory.getLogger(StsUtil.class);

    private static final String APP_KEY = EnvironmentUtil.getProperty("oss.accessKeyId");
    private static final String APP_SEC = EnvironmentUtil.getProperty("oss.accessKeySecret");
    private static final String APP_ROLE = EnvironmentUtil.getProperty("oss.sts.roleArn");
    private static final String ENDPOINT = EnvironmentUtil.getProperty("oss.sts.endpoint");

    public static Map<String, Object> getStsTokenMap() {
        String token = null;
        String roleSessionName = "chips-client-sts";

        Map<String, Object> params = new HashMap<>();


        try {
            // Init ACS Client
            DefaultProfile.addEndpoint("", "", "Sts", ENDPOINT);
            IClientProfile profile = DefaultProfile.getProfile("", APP_KEY, APP_SEC);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(APP_ROLE);
            request.setRoleSessionName(roleSessionName);
            //request.setPolicy(policy); // Optional

            // 设置 DefaultAcsClient 请求超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");

            final AssumeRoleResponse response = client.getAcsResponse(request);
            if (response != null) {
                params.put("StatusCode", 200);
                token = response.getCredentials().getSecurityToken();
                params.put("SecurityToken", response.getCredentials().getSecurityToken());
                params.put("Expiration", response.getCredentials().getExpiration());
                params.put("AccessKeyId", response.getCredentials().getAccessKeyId());
                params.put("AccessKeySecret", response.getCredentials().getAccessKeySecret());
            }


            logger.debug("--->come in getStsTokenMap token is: " + token);

        } catch (ClientException e) {

            logger.error("--->come in getStsTokenMap Failed  error:{} ", e.getErrMsg(), e);
            params = new HashMap<>();
            params.put("StatusCode", 500);
            params.put("ErrorCode", e.getErrCode());
            params.put("ErrorMessage", e.getErrMsg());
        }

        return params;
    }

    public static String getStsToken() {
        String token = null;
        String roleSessionName = "chips-client-sts";

        try {
            // Init ACS Client
            DefaultProfile.addEndpoint("", "", "Sts", ENDPOINT);
            IClientProfile profile = DefaultProfile.getProfile("", APP_KEY, APP_SEC);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(APP_ROLE);
            request.setRoleSessionName(roleSessionName);
            //request.setPolicy(policy); // Optional
            final AssumeRoleResponse response = client.getAcsResponse(request);
            if (response != null) {
                token = response.getCredentials().getSecurityToken();
            }
            logger.debug("--->come in getStsToken token is: " + token);
        } catch (ClientException e) {
            logger.error("--->come in getStsToken Failed  error:{} ", e.getErrMsg(), e);
        }

        return token;
    }

    public static Map<String, String> getPolicyMap(String dir) {
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        String endpoint = "oss-cn-hongkong.aliyuncs.com";
        String accessId = APP_KEY;
        String bucket = "wallet";
        String host = "https://" + bucket + "." + endpoint;
        OSSClient client = new OSSClient(endpoint, accessId, APP_SEC);
        try {
            long expireTime = 300;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            respMap.put("accessId", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            JSONObject ja1 = JSONObject.fromObject(respMap);
            logger.debug("--->come in getPolicyMap respMap is:{} ", ja1.toString());
        } catch (Exception e) {
            logger.error("--->come in getPolicyMap error msg is ", e);
        }
        return respMap;
    }

}
