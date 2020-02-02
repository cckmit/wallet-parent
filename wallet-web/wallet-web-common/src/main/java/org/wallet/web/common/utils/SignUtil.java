package org.wallet.web.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.WebConstants;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author zengfucheng
 **/
public class SignUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);
    private static final Long TIME_RANGE = 1000L * 60 * 10;

    /**
     * 检查请求参数是否签名正确
     * @param request 请求
     * @param keyName Key参数名
     * @param keyValue Key参数值
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean checkSign(HttpServletRequest request, String keyName, String keyValue){
        String uri = request.getRequestURI();
        Map<String, Object> paramMap;
        try {
            paramMap = getRequestParamMap(request);

            if(null == paramMap){
                logger.warn("[{}]PARAMETER_NOT_EXIST", uri);
                return false;
            }

            Object t = paramMap.get(WebConstants.PARAM_TIME);
            if(StringUtils.isEmpty(t) || !t.toString().matches("\\d+")){
                logger.warn("[{}]ILLEGAL_TIME[{}][{}]", uri, WebConstants.PARAM_TIME, t);
                return false;
            }

            Long time = Long.parseLong(t.toString());

            long start = System.currentTimeMillis() - TIME_RANGE;
            long end = System.currentTimeMillis() + TIME_RANGE;

            if(time < start || time > end){
                logger.warn("[{}]TIME[{}]OVER_RANGE[{}:{}]", uri, time, start, end);
                return false;
            }

            String reqSign = (String) paramMap.get(WebConstants.PARAM_SIGN);
            paramMap.remove(WebConstants.PARAM_SIGN);

            Map<String, String> charMap = (Map<String, String>) paramMap.get(WebConstants.PARAM_CHAR);
            paramMap.remove(WebConstants.PARAM_CHAR);

            String beforeEncodeParamString = concatParam(paramMap, charMap, keyName, keyValue);

            String sign = getSign(beforeEncodeParamString);

            if(StringUtils.isEmpty(reqSign)){
                logger.warn("[{}]SIGN_NOT_EXIST", uri);
                return false;
            }

            boolean result = reqSign.equalsIgnoreCase(sign);
            if(!result){
                logger.info("[{}]SIGN_PARAM:{}", uri, beforeEncodeParamString);
                logger.info("[{}]SIGN_ENCODE:{}", uri, URLEncoder.encode(beforeEncodeParamString, "UTF-8").replaceAll("\\+",  "%20").replaceAll("\\*",  "%2A"));
                paramMap.put(WebConstants.PARAM_SIGN, reqSign);
                if(!CollectionUtils.isEmpty(charMap)){
                    paramMap.put(WebConstants.PARAM_CHAR, charMap);
                }
                logger.info("[{}]ILLEGAL_SIGN[{}][{}][{}][{}]", request.getRequestURI(), request.getMethod(), request.getContentType(), sign, JSON.toJSONString(paramMap));
            }
            return result;
        } catch (Exception e) {
            logger.warn("[{}]验证签名失败：{}", uri, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获取签名
     * @param result 参数
     * @return
     */
    public static String getSign(String result){
        try {
            result = URLEncoder.encode(result, "UTF-8");
            result = result.replaceAll("\\+",  "%20").replaceAll("\\*",  "%2A");
        } catch (UnsupportedEncodingException e) {
            logger.error("转码失败：" + e.getMessage(), e);
        }
        result = md5(result).toUpperCase();
        return result;
    }

    /**
     * 拼接参数
     * @param map 参数
     * @param charMap 替换字符Map
     * @param keyName 关键字参数名
     * @param keyValue 关键字参数值
     * @return
     */
    private static String concatParam(Map<String, Object> map, Map<String, String> charMap, String keyName, String keyValue) {
        ArrayList<String> list = new ArrayList<>();
        for(Map.Entry<String, Object> entry : map.entrySet()){
            Object value = entry.getValue();
            if(!StringUtils.isEmpty(value)){
                String string = value.toString();
                if(!CollectionUtils.isEmpty(charMap)){
                    string = replaceChar(string, charMap);
                }
                list.add(entry.getKey() + "=" + string + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        if(!StringUtils.isEmpty(keyValue)){
            result += keyName + "=" + keyValue;
        }
        return result;
    }

    /**
     * 替换字符串中的特殊字符
     * @param string 值
     * @param map 替换字符串
     * @return
     */
    private static String replaceChar(String string, Map<String, String> map){
        if(StringUtils.isEmpty(string) || CollectionUtils.isEmpty(map)){
            return string;
        }
        for(Map.Entry<String, String> entry : map.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            string = string.replaceAll(key, value);
        }
        return string;
    }

    /**
     * 获取请求参数Map
     * @param request 请求
     * @return 请求参数Map
     */
    public static Map<String, Object> getRequestParamMap(HttpServletRequest request) throws IOException {
        Map<String, String[]> allMap = request.getParameterMap();
        Map<String, Object> map = new HashMap<>(allMap.size());
        for (Map.Entry<String, String[]> entry: allMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue()[0]);
        }

        if(!HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())){
            request.setCharacterEncoding("UTF-8");
            String body = getBodyString(request.getInputStream());
            if(!StringUtils.isEmpty(body)){
                try {
                    Map<String, Object> jsonObject = JSON.parseObject(body, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField);
                    map.putAll(jsonObject);
                } catch (JSONException e){
                    logger.warn("BODY STRING NOT VALID JSON: {}", body);
                }
            }
        }

        return map;
    }

    public static String getBodyString(ServletInputStream inputStream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String line;
            while((line = reader.readLine())!=null){
                buffer.append(line);
            }
        } finally{
            if(null != reader){
                reader.close();
            }
        }
        return buffer.toString();
    }

    public static String md5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("MD5 error:" + e.getMessage(), e);
            return "";
        }
    }
}
