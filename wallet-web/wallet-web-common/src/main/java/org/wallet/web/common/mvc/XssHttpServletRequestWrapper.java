package org.wallet.web.common.mvc;

import jodd.io.StreamUtil;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Xss包装器
 * @author zengfucheng
 *
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	/**
	 * 用于保存读取Request body 中数据
	 */
	private final byte[] body;

	private final boolean exclude;

    public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String contentType = request.getContentType();
        exclude = !StringUtils.isEmpty(contentType)
                && (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                || contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE));

        if(exclude){
            body = new byte[]{};
        }else{
            body = StreamUtil.readBytes(request.getReader(), "UTF-8");
        }
    }  
  
	@Override
    public Map<String, String[]> getParameterMap() {
    	Map<String, String[]> newParaMap = new HashMap<>(10);
		Map<String, String[]> paramMap = super.getParameterMap();
    	int len;
		String[] oldValues;
		String[] newValues;
		for(String key : paramMap.keySet()) {
			oldValues = paramMap.get(key);
			len = oldValues.length;
			newValues = new String[len];
			for(int i=0; i< len; i++) {
				newValues[i] = xssEncode(oldValues[i]);
			}
			newParaMap.put(key, newValues);
		}
    	return newParaMap;
    }
    
    /**
     *  对参数值进行Xss编码
     */
    @Override
    public String[] getParameterValues(String parameter) {
    	String[] orgParamVal = super.getParameterValues(xssEncode(parameter));
    	if(orgParamVal != null)  {
    		int len = orgParamVal.length;
        	String[] encodeParamVal = new String[len];
        	for(int i=0; i<len; i++) {
        		encodeParamVal[i] = xssEncode(orgParamVal[i]);
        	}
        	return encodeParamVal;
    	}
    	return null;
    }
    
    /** 
     * 覆盖getParameter方法，将参数名和参数值都做安全处理
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖 
     */  
    @Override  
    public String getParameter(String name) {  
        String value = super.getParameter(xssEncode(name));  
        if (value != null) {  
            value = xssEncode(value);  
        }  
        return value;  
    }  
  
    /** 
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取
     * getHeaderNames 也可能需要覆盖 
     */  
    @Override  
    public String getHeader(String name) {  
        String value = super.getHeader(xssEncode(name));  
        if (value != null) {  
            value = xssEncode(value);  
        }  
        return value;  
    }  
  
    private String xssEncode(String s) {
    	if(s == null || s.isEmpty()) {
            return s;
        } else {
            return HtmlUtils.htmlEscape(s, "UTF-8");
        }
    }

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
        if(exclude){
            return super.getInputStream();
        }
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			@Override
			public int read() {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}
		};
	}
}
